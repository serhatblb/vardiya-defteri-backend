package com.kardemir.vardiyadefteri.service;

public interface SystemSettingService {
    String getValue(String key);
    void setValue(String key, String value);

    // 🔧 Yeni: Sayısal ayarları güvenle çekmek için
    int getIntValue(String key, int defaultValue);
}
