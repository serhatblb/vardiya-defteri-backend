package com.kardemir.vardiyadefteri.dto;

import com.kardemir.vardiyadefteri.entity.VardiyaType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VardiyaDTO {
    private Long id;
    private Long userId;

    @NotNull
    private VardiyaType vardiyaType;

    @NotNull
    private LocalDate tarih;

    // opsiyonel, sistem kendi ekler
    private LocalDateTime saat;

    @NotBlank
    @Size(min = 5)
    private String notIcerik;

    // opsiyonel, sistem kendi ekler
    private LocalDateTime olusturmaTarihi;
    private String adSoyad;


    private String unite;

    private String guncellemeTarihi;

}
