package com.kardemir.vardiyadefteri.service.impl;

import com.kardemir.vardiyadefteri.entity.SystemSetting;
import com.kardemir.vardiyadefteri.repository.SystemSettingRepository;
import com.kardemir.vardiyadefteri.service.SystemSettingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SystemSettingServiceImpl implements SystemSettingService {

    private final SystemSettingRepository repo;

    public SystemSettingServiceImpl(SystemSettingRepository repo) {
        this.repo = repo;
    }

    @Override
    public String getValue(String key) {
        return repo.findBySettingKey(key)
                .map(SystemSetting::getSettingValue)
                .orElse(null);
    }

    @Override
    public void setValue(String key, String value) {
        SystemSetting setting = repo.findBySettingKey(key)
                .orElse(new SystemSetting(key, value));
        setting.setSettingValue(value);
        repo.save(setting);
    }

    // 🔒 Yeni metod: Güvenli integer okuma
    public int getIntValue(String key, int defaultValue) {
        String value = getValue(key);
        try {
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
