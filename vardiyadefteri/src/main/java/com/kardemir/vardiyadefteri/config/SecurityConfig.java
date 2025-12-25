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
                // CORS Ayarını devreye alıyoruz
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // CSRF kapalı (API'ler için gerekli)
                .csrf(AbstractHttpConfigurer::disable)
                // Session yok (JWT kullandığımız için Stateless)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // 1. Herkese Açık Olanlar (Login ve Public Ayarlar)
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/settings/public").permitAll()

                        // Swagger/OpenAPI kullanıyorsan onlara da izin ver (İsteğe bağlı)
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // 2. Kullanıcı İşlemleri
                        .requestMatchers(HttpMethod.PATCH, "/api/users/{id}/unblock").hasRole("SISTEM_YONETICISI")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("SISTEM_YONETICISI")
                        .requestMatchers(HttpMethod.GET, "/api/users/**").hasAnyRole("SISTEM_YONETICISI", "ISLETME_SORUMLUSU")
                        .requestMatchers(HttpMethod.POST, "/api/users/**").hasAnyRole("SISTEM_YONETICISI", "ISLETME_SORUMLUSU")
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").hasRole("SISTEM_YONETICISI")

                        // 3. Vardiya İşlemleri
                        .requestMatchers(HttpMethod.GET, "/api/vardiyas/user/**")
                        .hasAnyRole("SISTEM_YONETICISI", "ISLETME_SORUMLUSU", "NORMAL_KULLANICI")

                        .requestMatchers(HttpMethod.GET, "/api/vardiyas/**")
                        .hasAnyRole("SISTEM_YONETICISI", "ISLETME_SORUMLUSU")

                        .requestMatchers(HttpMethod.POST, "/api/vardiyas/**")
                        .hasAnyRole("SISTEM_YONETICISI", "ISLETME_SORUMLUSU")

                        .requestMatchers(HttpMethod.PUT, "/api/vardiyas/**")
                        .hasAnyRole("SISTEM_YONETICISI", "ISLETME_SORUMLUSU")

                        // 4. Sistem Ayarları
                        .requestMatchers(HttpMethod.GET, "/api/settings").hasRole("SISTEM_YONETICISI")
                        .requestMatchers(HttpMethod.POST, "/api/settings").hasRole("SISTEM_YONETICISI")

                        // Diğer her şey kimlik doğrulama ister
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * 🔥 KRİTİK CORS AYARI 🔥
     * Burası Vercel ve Render'ın konuşmasını sağlayan yerdir.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();

        // ÖNEMLİ: Sadece localhost değil, her yerden gelen isteği kabul et (Pattern kullanıyoruz)
        // allowedOrigins("*") hata verir, o yüzden allowedOriginPatterns("*") kullanıyoruz.
        cfg.setAllowedOriginPatterns(List.of("*"));

        // İzin verilen metodlar
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        // İzin verilen başlıklar (Token vs.)
        cfg.setAllowedHeaders(List.of("*"));

        // Credentials (Çerez/Token taşınmasına izin ver) - Bunu true yaptık!
        cfg.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }
}