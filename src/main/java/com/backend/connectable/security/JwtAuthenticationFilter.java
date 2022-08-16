package com.backend.connectable.security;

import com.backend.connectable.security.exception.ConnectableSecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = AuthorizationExtractor.extract(request);
        String path = request.getRequestURI();
        log.info("[[Token]] : " + token);
        log.info("[[Path]] : " + path);
        try {
            if (!Objects.isNull(token)) {
                verifyTokenAccordingToPath(token, path);
            }
            filterChain.doFilter(request, response);
        } catch (ConnectableSecurityException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(e.getMessage());
        }
    }

    private void verifyTokenAccordingToPath(String token, String path) throws ConnectableSecurityException {
        if (path.startsWith("/admin")) {
            jwtProvider.verifyAdmin(token);
            return;
        }
        jwtProvider.verify(token);
        Authentication authentication = jwtProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
