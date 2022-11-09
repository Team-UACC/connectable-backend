package com.backend.connectable.security.config;

import static org.springframework.http.HttpMethod.*;

import com.backend.connectable.security.custom.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
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
                .anyRequest()
                .permitAll()
                .and()
                .addFilterBefore(
                        jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
