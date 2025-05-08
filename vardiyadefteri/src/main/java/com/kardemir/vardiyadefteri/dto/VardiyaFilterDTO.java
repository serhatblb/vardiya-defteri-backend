package com.kardemir.vardiyadefteri.dto;

import com.kardemir.vardiyadefteri.entity.VardiyaType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class VardiyaFilterDTO {
    private VardiyaType vardiyaType; // örn: SAAT_08_16
    private String unite;            // ünite adı
    private String yazarAdSoyad;     // kullanıcı adı filtreleme
    private LocalDate baslangicTarihi;
    private LocalDate bitisTarihi;
}
