package com.kardemir.vardiyadefteri.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String sicil;

    @Column(nullable = false)
    private String ad;

    @Column(nullable = false)
    private String soyad;

    private String unvan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Unite unite;

    @Column(nullable = false)
    private String sifre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    private LocalDateTime hesapAcilisTarihi;
    private LocalDateTime hesapSilmeTarihi;

    private String silinmeNedeni;

    // Yeni güvenlik alanları
    @Column(nullable = false)
    private boolean blokeMi = false;

    private LocalDateTime sonBlokeTarihi;

    @Column(nullable = false)
    private int hataliGirisSayisi = 0;
}
