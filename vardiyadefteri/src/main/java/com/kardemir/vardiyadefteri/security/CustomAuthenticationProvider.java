package com.kardemir.vardiyadefteri.security;

import com.kardemir.vardiyadefteri.entity.User;
import com.kardemir.vardiyadefteri.repository.UserRepository;
import com.kardemir.vardiyadefteri.service.SystemSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SystemSettingService settingService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String sicil = authentication.getName();
        String rawPassword = authentication.getCredentials().toString();

        // ---- LOG: her denemede görülsün ----
        System.out.println("[CustomAuth] authenticate() çağrıldı: " + sicil);

        // ---- 1) Kullanıcı var mı kontrol et ----
        Optional<User> optionalUser = userRepository.findBySicil(sicil);
        if (optionalUser.isEmpty()) {
            System.out.println("[CustomAuth] Kullanıcı bulunamadı: " + sicil);
            throw new BadCredentialsException("Kullanıcı bulunamadı.");
        }

        User user = optionalUser.get();

        // ---- 2) Sayaç ve limit bilgilerini al, logla ----
        int currentAttempts = user.getHataliGirisSayisi();
        int maxAttempts     = settingService.getIntValue("MAX_FAILED_ATTEMPTS", 3);
        System.out.println(String.format(
                "[CustomAuth] Bulunan user ⇒ hataliGiris=%d, maxAttempts=%d, blokeMi=%s",
                currentAttempts, maxAttempts, user.isBlokeMi()
        ));

        // ---- 3) Zaten blokeli mi? ----
        if (user.isBlokeMi()) {
            System.out.println("[CustomAuth] Kullanıcı zaten bloke: " + sicil);
            throw new LockedException("Hesabınız bloke edilmiştir.");
        }

        // ---- 4) Şifre kontrolü ----
        if (!passwordEncoder.matches(rawPassword, user.getSifre())) {
            currentAttempts++;
            user.setHataliGirisSayisi(currentAttempts);

            if (currentAttempts >= maxAttempts) {
                user.setBlokeMi(true);
                user.setSonBlokeTarihi(LocalDateTime.now());
                userRepository.save(user);
                System.out.println("[CustomAuth] Bloke edildi: " + sicil + " (count=" + currentAttempts + ")");
                throw new LockedException("Maksimum hatalı giriş yapıldı, kullanıcı bloke edildi.");
            } else {
                userRepository.save(user);
                int remaining = maxAttempts - currentAttempts;
                System.out.println("[CustomAuth] Hatalı şifre. Yeni count=" + currentAttempts +
                        ", kalanHak=" + remaining);
                throw new BadCredentialsException("Geçersiz şifre. Kalan hak: " + remaining);
            }
        }

        // ---- 5) Başarılı girişse sayaç sıfırla ----
        if (currentAttempts > 0) {
            user.setHataliGirisSayisi(0);
            userRepository.save(user);
            System.out.println("[CustomAuth] Başarılı giriş. Sayaç sıfırlandı.");
        }

        // ---- 6) AuthenticationToken üret ve dön ----
        var userDetails = new CustomUserDetails(user);
        return new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
