package com.kardemir.vardiyadefteri.config;

import com.kardemir.vardiyadefteri.security.CustomAuthenticationProvider;
import com.kardemir.vardiyadefteri.security.JwtAuthenticationFilter;
import com.kardemir.vardiyadefteri.repository.UserRepository;
import com.kardemir.vardiyadefteri.service.SystemSettingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            SystemSettingService settingService
    ) {
        return new CustomAuthenticationProvider(userRepository, passwordEncoder, settingService);
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig
    ) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            CustomAuthenticationProvider customAuthProvider,
            JwtAuthenticationFilter jwtFilter
    ) throws Exception {
        http
                .authenticationProvider(customAuthProvider)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/settings/public").permitAll()

                        // Kullanıcı işlemleri
                        .requestMatchers(HttpMethod.PATCH, "/api/users/{id}/unblock").hasRole("SISTEM_YONETICISI")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("SISTEM_YONETICISI")
                        .requestMatchers(HttpMethod.GET, "/api/users/**").hasAnyRole("SISTEM_YONETICISI", "ISLETME_SORUMLUSU")
                        .requestMatchers(HttpMethod.POST, "/api/users/**").hasAnyRole("SISTEM_YONETICISI", "ISLETME_SORUMLUSU")
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").hasRole("SISTEM_YONETICISI")

                        // Vardiya işlemleri
                        .requestMatchers(HttpMethod.GET, "/api/vardiyas/user/**")
                        .hasAnyRole("SISTEM_YONETICISI", "ISLETME_SORUMLUSU", "NORMAL_KULLANICI")

                        .requestMatchers(HttpMethod.GET, "/api/vardiyas/**")
                        .hasAnyRole("SISTEM_YONETICISI", "ISLETME_SORUMLUSU")

                        .requestMatchers(HttpMethod.POST, "/api/vardiyas/**")
                        .hasAnyRole("SISTEM_YONETICISI", "ISLETME_SORUMLUSU")

                        .requestMatchers(HttpMethod.PUT, "/api/vardiyas/**")
                        .hasAnyRole("SISTEM_YONETICISI", "ISLETME_SORUMLUSU")

                        // Sistem ayarları
                        .requestMatchers(HttpMethod.GET, "/api/settings").hasRole("SISTEM_YONETICISI")
                        .requestMatchers(HttpMethod.POST, "/api/settings").hasRole("SISTEM_YONETICISI")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(List.of("http://localhost:4200"));
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        cfg.setAllowedHeaders(List.of("*"));
        cfg.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }
}