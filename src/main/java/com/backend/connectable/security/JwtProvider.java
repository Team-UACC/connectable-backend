package com.backend.connectable.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
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

    @PostConstruct
    public void initializeAlgorithm() {
        algorithm = Algorithm.HMAC256(secretKey);
    }

    public String generateToken(String payload) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + duration);

        return JWT.create()
                .withPayload(Collections.singletonMap("walletAddress", payload))
                .withExpiresAt(validity)
                .sign(algorithm);
    }

    // ToDo jwt Token 검증 -> boolean

    // ToDo payload 추출
}
