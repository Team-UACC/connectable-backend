package com.backend.connectable.security.config;

import static com.backend.connectable.exception.ErrorType.SECURITY_EXCEPTION;
import static org.springframework.http.HttpMethod.*;

import com.backend.connectable.exception.ExceptionResponse;
import com.backend.connectable.security.custom.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final ObjectMapper objectMapper;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .disable()
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(OPTIONS, "/**/*")
                .permitAll()
                .antMatchers("/users/login", "/events", "/events/**", "/auth/sms/**")
                .permitAll()
                .antMatchers("/users", "/users/tickets", "/orders", "/orders/**")
                .authenticated()
                .antMatchers(POST, "/artists/{artist-id}/comments")
                .authenticated()
                .antMatchers(DELETE, "/artists/{artist-id}/comments")
                .authenticated()
                .antMatchers(GET, "/artists/{artist-id}/owner")
                .authenticated()
                .anyRequest()
                .permitAll()
                .and()
                .addFilterBefore(
                        jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        securityExceptionHandlingConfig(http);
    }

    private void securityExceptionHandlingConfig(HttpSecurity http) throws Exception {
        http.exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, exception) -> {
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter()
                                    .write(
                                            objectMapper.writeValueAsString(
                                                    ExceptionResponse.of(SECURITY_EXCEPTION)));
                        });
    }
}
