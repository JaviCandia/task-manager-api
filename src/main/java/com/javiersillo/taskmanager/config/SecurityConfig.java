package com.javiersillo.taskmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll() // Permite H2 Console
                        .anyRequest().permitAll() // Permite TODO lo demás (por ahora)
                )
                .csrf(AbstractHttpConfigurer::disable) // Deshabilita CSRF (común para APIs REST)
                // .headers(headers -> headers.frameOptions().disable()); // <-- ¡Esta línea está DEPRECATED!
                .headers(headers -> headers // <-- ¡Nueva forma de configurar los headers!
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable) // <-- Así se deshabilita frameOptions ahora
                );

        return http.build();
    }
}