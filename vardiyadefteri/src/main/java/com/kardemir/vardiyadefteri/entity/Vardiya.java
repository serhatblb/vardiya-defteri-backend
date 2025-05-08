package com.kardemir.vardiyadefteri.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "vardiya",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_user_tarih_vardiya",
                columnNames = {"user_id", "tarih", "vardiya_type"}
        )
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vardiya {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Hangi kullanıcı girdi
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Sabah/Öğle/Akşam/Gece
    @Enumerated(EnumType.STRING)
    @Column(name = "vardiya_type", nullable = false)
    private VardiyaType vardiyaType;

    // Tarih kısmı (sadece gün)
    @Column(nullable = false)
    private LocalDate tarih;

    // Saat ve dakika
    @Column(nullable = false)
    private LocalDateTime saat;

    // Kullanıcının notu
    @Column(columnDefinition = "TEXT", nullable = false)
    private String notIcerik;

    // Oluşturulma zamanı
    @Column(nullable = false)
    private LocalDateTime olusturmaTarihi;

    // Soft delete için silinme tarihi
    @Column(name = "hesap_silme_tarihi")
    private LocalDateTime hesapSilmeTarihi;

    @Enumerated(EnumType.STRING)
    @Column(name = "unite")
    private Unite unite;

    private LocalDateTime guncellemeTarihi;


}
