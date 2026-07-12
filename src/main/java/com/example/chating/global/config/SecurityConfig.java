package com.example.chating.global.config;

import com.example.chating.global.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .formLogin(formLogin -> formLogin.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/spaces").permitAll()
                        .requestMatchers(HttpMethod.GET, "/spaces/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/spaces/*/schedule").permitAll()

                        .requestMatchers(HttpMethod.POST, "/spaces").authenticated()
                        .requestMatchers(HttpMethod.GET, "/spaces/me").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/spaces/*").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/spaces/*").authenticated()

                        .requestMatchers(HttpMethod.POST, "/spaces/*/schedule").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/spaces/*/schedule/*").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/spaces/*/schedule/*").authenticated()

                        .anyRequest().permitAll()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}