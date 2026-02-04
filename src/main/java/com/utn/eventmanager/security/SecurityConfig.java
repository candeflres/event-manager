package com.utn.eventmanager.security;

import com.utn.eventmanager.service.user.UserService;
import com.utn.eventmanager.service.user.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // ===== PUBLIC =====
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/public/**").permitAll()
                        .requestMatchers("/api/bot/**").permitAll()

                        // USERS
                        .requestMatchers(HttpMethod.POST, "/api/users/**").permitAll()

                        // ELEMENTS & OPTIONS (INFO PUBLICA)
                        .requestMatchers(HttpMethod.GET, "/api/elements/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/options/**").permitAll()

                        // ===== PROTECTED =====
                        .requestMatchers(HttpMethod.POST, "/api/elements/**")
                        .hasAnyRole("EMPLOYEE", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/elements/**")
                        .hasAnyRole("EMPLOYEE", "ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/options/**")
                        .hasAnyRole("EMPLOYEE", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/options/**")
                        .hasAnyRole("EMPLOYEE", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/options/**")
                        .hasAnyRole("EMPLOYEE", "ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/audit/**")
                        .hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .userDetailsService(userDetailsService)
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(sess ->
                        sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }
}