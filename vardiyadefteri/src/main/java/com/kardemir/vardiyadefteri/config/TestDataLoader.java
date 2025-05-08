package com.kardemir.vardiyadefteri.config;

import com.kardemir.vardiyadefteri.entity.Rol;
import com.kardemir.vardiyadefteri.entity.Unite;
import com.kardemir.vardiyadefteri.entity.User;
import com.kardemir.vardiyadefteri.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class TestDataLoader {

    private final UserRepository userRepository;

    @Bean
    public CommandLineRunner loadData() {
        return args -> {
            if (userRepository.count() == 0) {
                userRepository.save(User.builder()
                        .sicil("1001")
                        .ad("Ali")
                        .soyad("Yılmaz")
                        .unvan("Mühendis")
                        .unite(Unite.valueOf("KONVERTOR"))
                        .sifre("1234")
                        .rol(Rol.SISTEM_YONETICISI)
                        .hesapAcilisTarihi(LocalDateTime.now())
                        .blokeMi(false)
                        .hataliGirisSayisi(0)
                        .build()
                );
                System.out.println("Test kullanıcısı eklendi.");
            }
        };
    }
}
