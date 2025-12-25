package com.kardemir.vardiyadefteri.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;

public enum VardiyaType {
    SAAT_08_16("08:00-16:00"),
    SAAT_16_00("16:00-00:00"),
    SAAT_00_08("00:00-08:00"),
    SAAT_00_12("00:00-12:00"),
    SAAT_12_24("12:00-24:00");

    private final String period;

    VardiyaType(String period) {
        this.period = period;
    }

    @JsonValue
    public String getPeriod() {
        return period;
    }

    @JsonCreator
    public static VardiyaType fromString(String value) {
        for (VardiyaType type : VardiyaType.values()) {
            if (type.name().equalsIgnoreCase(value.trim())) {
                return type;
            }
        }
        throw new IllegalArgumentException("Geçersiz vardiya tipi: " + value);
    }
}
