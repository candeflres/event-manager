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
    @Order(1)
    public SecurityFilterChain publicApi(HttpSecurity http) throws Exception {
        http
                .securityMatcher(
                        "/api/elements/**",
                        "/api/options/**",
                        "/api/public/**",
                        "/api/auth/**",
                        "/api/users"
                )
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/elements/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/options/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/public/**").permitAll()
                        .anyRequest().permitAll()
                );

        return http.build();
    }
    @Bean
    @Order(2)
    public SecurityFilterChain securedApi(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // ELEMENTS
                        .requestMatchers(HttpMethod.POST, "/api/elements/**").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.PUT, "/api/elements/**").hasRole("EMPLOYEE")
                        // OPTIONS
                        .requestMatchers(HttpMethod.POST, "/api/options/**").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.PUT, "/api/options/**").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.DELETE, "/api/options/**").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.POST, "/api/users/employees").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/*").hasRole("ADMIN")

                        // RESTO
                        .anyRequest().authenticated()
                )
                .userDetailsService(userDetailsService)
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(sess ->
                        sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/api/bot/**");
    }
}
