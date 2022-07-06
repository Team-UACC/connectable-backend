package com.backend.connectable.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expire-time}")
    private Long duration;

    private Algorithm algorithm;

    private static final String PAYLOAD_KEY = "klaytnAddress";

    @PostConstruct
    public void initializeAlgorithm() {
        algorithm = Algorithm.HMAC256(secretKey);
    }

    public String generateToken(String payload) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + duration);

        return JWT.create()
                .withPayload(Collections.singletonMap(PAYLOAD_KEY, payload))
                .withExpiresAt(validity)
                .sign(algorithm);
    }

    public Boolean verify(String token) {
        try {
            JWT.require(algorithm)
                .build()
                .verify(token);
        } catch (JWTVerificationException e) {
            return false;
        }
        return true;
    }

    // ToDo payload 추출
}
