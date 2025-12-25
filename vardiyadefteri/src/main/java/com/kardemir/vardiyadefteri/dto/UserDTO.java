package com.kardemir.vardiyadefteri.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String sicil;
    private String ad;
    private String soyad;
    private String unvan;
    private String unite;
    private String rol;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String sifre;               // ← Eklendi
    
    private boolean blokeMi;
    private LocalDateTime hesapAcilisTarihi;
    private LocalDateTime hesapSilmeTarihi;
    private String silinmeNedeni;
    private LocalDateTime sonBlokeTarihi;
}
