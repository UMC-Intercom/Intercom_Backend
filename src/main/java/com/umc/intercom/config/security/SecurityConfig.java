package com.umc.intercom.config.security;

import com.umc.intercom.config.jwt.JwtAuthenticationFilter;
import com.umc.intercom.config.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;

    // 인증하지 않아도 접근 가능
    private static final String[] PERMIT_URL_ARRAY = {
            /* swagger v2 */
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            /* swagger v3 */
            "/v3/api-docs/**",
            "/swagger-ui/**",

            // TODO 나중에 수정
            "/**"
    };

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    protected SecurityFilterChain config(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        return http
                .csrf(CsrfConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(PERMIT_URL_ARRAY)
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt Encoder 사용
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}