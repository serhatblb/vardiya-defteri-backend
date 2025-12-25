package com.kardemir.vardiyadefteri.repository;

import com.kardemir.vardiyadefteri.entity.Unite;
import com.kardemir.vardiyadefteri.entity.Vardiya;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface VardiyaRepository extends JpaRepository<Vardiya, Long> {

    List<Vardiya> findByUserId(Long userId);

    long countByTarih(LocalDate tarih);

    long countByUserId(Long userId);

    long countByUserIdAndTarih(Long userId, LocalDate tarih);

    List<Vardiya> findByUserIdAndHesapSilmeTarihiIsNull(Long userId);

    // <<< Bu satırı ekledik:
    List<Vardiya> findByUserUniteAndHesapSilmeTarihiIsNull(Unite unite);
}
