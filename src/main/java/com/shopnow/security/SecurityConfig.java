package com.shopnow.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()

                                .requestMatchers("/admin/**").permitAll()

                                .requestMatchers("/admin/categories/delete/{id}").permitAll()

                                .requestMatchers("/orders/my-orders").authenticated()

                                .requestMatchers("/checkout", "/orders/**").authenticated()

                                .requestMatchers("/", "/cart/**", "/category/**", "/attachments/**").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(form -> {

                });

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
