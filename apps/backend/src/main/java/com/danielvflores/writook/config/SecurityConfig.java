package com.danielvflores.writook.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.danielvflores.writook.security.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {
    
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @Value("${cors.allowed-origins:http://localhost:3000}")
    private String allowedOrigins;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/v1/auth/register",
                    "/api/v1/auth/login",
                    "/api/v1/health").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.GET,
                    "/api/v1/stories",
                    "/api/v1/stories/{id}",
                    "/api/v1/stories/{id}/stats",
                    "/api/v1/stories/with-stats",
                    "/api/v1/comments/stories/{id}",
                    "/api/v1/ratings/stories/{id}",
                    "/api/v1/chapters/stories/{storyId}/chapters/{chapterNumber}/stats",
                    "/api/v1/chapters/stories/{storyId}/chapters/{chapterNumber}/comments",
                    "/api/v1/chapters/stories/{storyId}/chapters/{chapterNumber}/ratings").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Parse allowed origins from environment variable (comma-separated)
        String[] origins = allowedOrigins.split(",");
        configuration.setAllowedOrigins(Arrays.asList(origins));
        
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // Password encoder bean to hash passwords using BCrypt.
    // AuthService will autowire PasswordEncoder to encode and verify passwords.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}