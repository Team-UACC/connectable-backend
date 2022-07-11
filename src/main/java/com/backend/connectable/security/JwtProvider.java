package com.backend.connectable.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    private Algorithm algorithm;

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
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
    }

    public String exportClaim(String token) {
        try {
             return JWT.decode(token)
                .getSubject();
        } catch (JWTDecodeException e) {
            throw new IllegalArgumentException("토큰 디코딩에 실패하였습니다.");
        }
    }
}
