package com.kardemir.vardiyadefteri.config;

import com.kardemir.vardiyadefteri.entity.Rol;
import com.kardemir.vardiyadefteri.entity.Unite;
import com.kardemir.vardiyadefteri.entity.User;
import com.kardemir.vardiyadefteri.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class TestDataLoader {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner loadData() {
        return args -> {
            // 🧹 1. ADIM: Önce temizlik! Tabloyu komple boşaltıyoruz.
            // Böylece "Duplicate Key" hatası asla almayız.
            userRepository.deleteAll();
            System.out.println("🧹 Veritabanı temizlendi (Eski kullanıcılar silindi).");

            // ➕ 2. ADIM: Kullanıcı 1 (Sistem Yöneticisi - Admin)
            User admin = User.builder()
                    .sicil("10148") // Senin sicil
                    .ad("Serhat")
                    .soyad("Yılmaz")
                    .unvan("Sistem Mühendisi")
                    .unite(Unite.KONVERTOR)
                    .sifre(passwordEncoder.encode("1234")) // Şifre: 1234
                    .rol(Rol.SISTEM_YONETICISI)
                    .hesapAcilisTarihi(LocalDateTime.now())
                    .blokeMi(false)
                    .hataliGirisSayisi(0)
                    .build();
            userRepository.save(admin);
            System.out.println("✅ Admin kullanıcı (10148) oluşturuldu.");

            // ➕ 3. ADIM: Kullanıcı 2 (Normal Kullanıcı / İşletme Sorumlusu - Test için)
            User user2 = User.builder()
                    .sicil("2025") // Farklı bir sicil
                    .ad("Ahmet")
                    .soyad("Demir")
                    .unvan("Formen")
                    .unite(Unite.HADDEHANE) // Varsa böyle bir ünite, yoksa KONVERTOR yap
                    .sifre(passwordEncoder.encode("1234")) // Şifre: 1234
                    .rol(Rol.ISLETME_SORUMLUSU) // Farklı bir rol
                    .hesapAcilisTarihi(LocalDateTime.now())
                    .blokeMi(false)
                    .hataliGirisSayisi(0)
                    .build();
            userRepository.save(user2);
            System.out.println("✅ İkinci kullanıcı (2025) oluşturuldu.");
        };
    }
}