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
            if (userRepository.findBySicil("1001").isEmpty()) {
                userRepository.save(User.builder()
                        .sicil("10148")
                        .ad("Serhat")
                        .soyad("Yılmaz")
                        .unvan("Mühendis")
                        .unite(Unite.KONVERTOR)
                        .sifre(passwordEncoder.encode("1234")) // 🔐 HASH
                        .rol(Rol.SISTEM_YONETICISI)
                        .hesapAcilisTarihi(LocalDateTime.now())
                        .blokeMi(false)
                        .hataliGirisSayisi(0)
                        .build()
                );
                System.out.println("✅ Test admin kullanıcısı oluşturuldu.");
            }
        };
    }
}
