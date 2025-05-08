package com.kardemir.vardiyadefteri.controller;

import com.kardemir.vardiyadefteri.dto.AuthRequest;
import com.kardemir.vardiyadefteri.dto.AuthResponse;
import com.kardemir.vardiyadefteri.entity.User;
import com.kardemir.vardiyadefteri.repository.UserRepository;
import com.kardemir.vardiyadefteri.security.CustomUserDetailsService;
import com.kardemir.vardiyadefteri.security.JwtUtil;
import com.kardemir.vardiyadefteri.service.SystemSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SystemSettingService settingService; // EKLENDİ ✅

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        int maxLoginAttempts = settingService.getIntValue("MAX_FAILED_ATTEMPTS", 3); // EKLENDİ ✅

        User user = userRepository.findBySicil(req.getSicil()).orElse(null);

        if (user == null || user.getHesapSilmeTarihi() != null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Geçersiz kullanıcı.");
        }

        if (user.isBlokeMi()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Hesabınız bloke olmuştur.");
        }

        if (!passwordEncoder.matches(req.getSifre(), user.getSifre())) {
            user.setHataliGirisSayisi(user.getHataliGirisSayisi() + 1);

            if (user.getHataliGirisSayisi() >= maxLoginAttempts) {
                user.setBlokeMi(true);
                user.setSonBlokeTarihi(LocalDateTime.now());
            }

            userRepository.save(user);

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Geçersiz şifre. Hatalı giriş sayısı: " + user.getHataliGirisSayisi());
        }

        user.setHataliGirisSayisi(0);
        userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(req.getSicil());
        String token = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(
                new AuthResponse(token, user.getRol().name(), user.getId(), "Giriş başarılı")
        );
    }
}
