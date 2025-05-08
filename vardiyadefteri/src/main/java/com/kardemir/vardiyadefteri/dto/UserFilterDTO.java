package com.kardemir.vardiyadefteri.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserFilterDTO {
    private String ad;
    private String sicil;
    private String unvan;
    private String unite;
    private String rol;
    private Boolean blokeMi;
    private LocalDateTime baslangicTarihi;
    private LocalDateTime bitisTarihi;
}
