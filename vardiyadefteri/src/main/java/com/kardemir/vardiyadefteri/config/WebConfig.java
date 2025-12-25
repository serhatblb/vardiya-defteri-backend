package com.kardemir.vardiyadefteri.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Tüm endpoint'lere izin ver
                //.allowedOrigins("https://vardiya-defteri.vercel.app") // Sadece senin siteye izin ver (Güvenli olan)
                .allowedOriginPatterns("*") // HER YERE İZİN VER (En garanti yöntem, hata şansını sıfırlar)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Tüm metodlara izin ver
                .allowedHeaders("*") // Tüm başlıklara izin ver
                .allowCredentials(true); // Token/Cookie işlemlerine izin ver
    }
}