package com.kardemir.vardiyadefteri.controller;

import com.kardemir.vardiyadefteri.service.SystemSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class SettingsController {

    private final SystemSettingService settingService;
    private static final String KEY_MAX_FAILED = "MAX_FAILED_ATTEMPTS";
    private static final String KEY_MIN_LENGTH = "MIN_PASSWORD_LENGTH";
    private static final String KEY_DEFAULT_PASSWORD = "DEFAULT_PASSWORD";

    // Mevcut, admin-only:
    @GetMapping
    @PreAuthorize("hasRole('SISTEM_YONETICISI')")
    public ResponseEntity<Map<String, String>> getAllSettings() {
        return ResponseEntity.ok(Map.of(
                "maxFailedAttempts", settingService.getValue(KEY_MAX_FAILED),
                "minPasswordLength", settingService.getValue(KEY_MIN_LENGTH),
                "defaultPassword", settingService.getValue(KEY_DEFAULT_PASSWORD)
        ));
    }

    @PostMapping
    @PreAuthorize("hasRole('SISTEM_YONETICISI')")
    public ResponseEntity<?> updateSettings(@RequestBody Map<String, String> body) {
        settingService.setValue(KEY_MAX_FAILED, body.get("maxFailedAttempts"));
        settingService.setValue(KEY_MIN_LENGTH, body.get("minPasswordLength"));
        settingService.setValue(KEY_DEFAULT_PASSWORD, body.get("defaultPassword"));
        return ResponseEntity.ok().build();
    }

    // Yeni, oturum gerektirmeyen endpoint:
    @GetMapping("/public")
    public ResponseEntity<Map<String, String>> getPublicSettings() {
        return ResponseEntity.ok(Map.of(
                "maxFailedAttempts", settingService.getValue(KEY_MAX_FAILED)
        ));
    }
}
