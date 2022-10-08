package com.backend.connectable.security.custom;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class AuthorizationExtractor {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER_TYPE = "Bearer";

    private AuthorizationExtractor() {}

    public static String extract(HttpServletRequest request) {
        String authorization = request.getHeader(AUTHORIZATION);
        if (!Objects.isNull(authorization)
                && authorization.toLowerCase().startsWith(BEARER_TYPE.toLowerCase())) {
            String tokenValue = authorization.substring(BEARER_TYPE.length()).trim();
            int commaIndex = tokenValue.indexOf(',');
            if (commaIndex > 0) {
                tokenValue = tokenValue.substring(0, commaIndex);
            }
            return tokenValue;
        }
        return null;
    }
}
