package com.backend.connectable.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.backend.connectable.exception.ConnectableException;
import com.backend.connectable.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expire-time}")
    private Long duration;

    @Value("${jwt.admin-payload}")
    private String adminPayload;

    private Algorithm algorithm;

    private final UserDetailsService userDetailsService;

    @PostConstruct
    public void initializeAlgorithm() {
        algorithm = Algorithm.HMAC256(secretKey);
    }

    public String generateToken(String claim) {
        return JWT.create()
            .withSubject(claim)
            .withClaim("expire", Instant.now().getEpochSecond() + duration)
            .sign(algorithm);
    }

    public void verify(String token) {
        try {
            JWT.require(algorithm)
                .build()
                .verify(token);
        } catch (JWTVerificationException e) {
            throw new ConnectableException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_TOKEN);
        }
    }

    public String exportClaim(String token) {
        try {
             return JWT.decode(token)
                .getSubject();
        } catch (JWTDecodeException e) {
            throw new ConnectableException(HttpStatus.BAD_REQUEST, ErrorType.TOKEN_PAYLOAD_EXTRACTION_FAILURE);
        }
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(exportClaim(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public void verifyAdmin(String token) {
        String adminPayload = exportClaim(token);
        if (!this.adminPayload.equals(adminPayload)) {
            throw new ConnectableException(HttpStatus.BAD_REQUEST, ErrorType.ADMIN_TOKEN_VERIFY_FAILURE);
        }
    }
}
