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
import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class TestDataLoader {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner loadData() {
        return args -> {
            // 🧹 1. ADIM: Temizlik
            userRepository.deleteAll();
            System.out.println("🧹 Veritabanı temizlendi.");

            String ortakSifre = passwordEncoder.encode("1234"); // Hepsinin şifresi: 1234

            // --- SİSTEM YÖNETİCİLERİ ---
            User serhat = User.builder()
                    .sicil("10148")
                    .ad("Serhat")
                    .soyad("Bülbül")
                    .unvan("Mühendis")
                    .unite(Unite.KONVERTOR)
                    .sifre(ortakSifre)
                    .rol(Rol.SISTEM_YONETICISI)
                    .hesapAcilisTarihi(LocalDateTime.now())
                    .blokeMi(false).hataliGirisSayisi(0).build();

            User hatice = User.builder()
                    .sicil("1000")
                    .ad("Hatice")
                    .soyad("Eren")
                    .unvan("Mühendis")
                    .unite(Unite.KONVERTOR)
                    .sifre(ortakSifre)
                    .rol(Rol.SISTEM_YONETICISI)
                    .hesapAcilisTarihi(LocalDateTime.now())
                    .blokeMi(false).hataliGirisSayisi(0).build();

            User buse = User.builder()
                    .sicil("1001")
                    .ad("Buse")
                    .soyad("Özcen")
                    .unvan("Mühendis")
                    .unite(Unite.KONVERTOR)
                    .sifre(ortakSifre)
                    .rol(Rol.SISTEM_YONETICISI)
                    .hesapAcilisTarihi(LocalDateTime.now())
                    .blokeMi(false).hataliGirisSayisi(0).build();

            User adem = User.builder()
                    .sicil("1002")
                    .ad("Adem")
                    .soyad("Çetinkaya")
                    .unvan("Başmühendis")
                    .unite(Unite.KONVERTOR)
                    .sifre(ortakSifre)
                    .rol(Rol.SISTEM_YONETICISI)
                    .hesapAcilisTarihi(LocalDateTime.now())
                    .blokeMi(false).hataliGirisSayisi(0).build();

            // --- İŞLETME SORUMLUSU ---
            User hakan = User.builder()
                    .sicil("2001")
                    .ad("Hakan")
                    .soyad("Akıncı")
                    .unvan("Danışman")
                    .unite(Unite.KONVERTOR) // Varsa CELIKHANE de yapabilirsin
                    .sifre(ortakSifre)
                    .rol(Rol.ISLETME_SORUMLUSU)
                    .hesapAcilisTarihi(LocalDateTime.now())
                    .blokeMi(false).hataliGirisSayisi(0).build();

            // --- NORMAL KULLANICILAR ---
            User isci = User.builder()
                    .sicil("3001")
                    .ad("Mehmet")
                    .soyad("Demir")
                    .unvan("İşçi")
                    .unite(Unite.KONVERTOR)
                    .sifre(ortakSifre)
                    .rol(Rol.NORMAL_KULLANICI)
                    .hesapAcilisTarihi(LocalDateTime.now())
                    .blokeMi(false).hataliGirisSayisi(0).build();

            User formen = User.builder()
                    .sicil("3002")
                    .ad("Ali")
                    .soyad("Çelik")
                    .unvan("Formen")
                    .unite(Unite.KONVERTOR)
                    .sifre(ortakSifre)
                    .rol(Rol.NORMAL_KULLANICI)
                    .hesapAcilisTarihi(LocalDateTime.now())
                    .blokeMi(false).hataliGirisSayisi(0).build();

            // Hepsini kaydet
            userRepository.saveAll(Arrays.asList(serhat, hatice, buse, adem, hakan, isci, formen));

            System.out.println("✅ Tüm test kullanıcıları (7 Kişi) başarıyla oluşturuldu.");
        };
    }
}