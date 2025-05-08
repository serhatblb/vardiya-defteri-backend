package com.kardemir.vardiyadefteri.service.impl;

import com.kardemir.vardiyadefteri.dto.UserDTO;
import com.kardemir.vardiyadefteri.dto.UserFilterDTO;
import com.kardemir.vardiyadefteri.entity.User;
import com.kardemir.vardiyadefteri.mapper.UserMapper;
import com.kardemir.vardiyadefteri.repository.UserRepository;
import com.kardemir.vardiyadefteri.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserDTO> getAllUsers() {
        log.info("Tüm kullanıcılar getiriliyor...");
        List<User> users = userRepository.findAll();
        log.debug("Veritabanından gelen kullanıcı sayısı: {}", users.size());
        return userMapper.toDtoList(users);
    }

    @Override
    public UserDTO getUserById(Long id) {
        log.info("ID ile kullanıcı aranıyor: {}", id);
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> {
                    log.warn("Kullanıcı bulunamadı, ID: {}", id);
                    return new RuntimeException("Kullanıcı bulunamadı");
                });
    }

    @Override
    public UserDTO createUser(UserDTO dto) {
        log.info("🟡 Yeni kullanıcı DTO alındı: {}", dto);
        try {
            User user = userMapper.toEntity(dto);
            log.info("🟢 DTO → Entity dönüşümü başarılı: {}", user);

            user.setHesapAcilisTarihi(LocalDateTime.now());
            user.setBlokeMi(false);
            user.setHataliGirisSayisi(0);

            // 🔐 Şifreyi hash'le
            String hashedPassword = passwordEncoder.encode(dto.getSifre());
            user.setSifre(hashedPassword);

            User saved = userRepository.save(user);
            log.info("✅ Kullanıcı veritabanına kaydedildi: {}", saved.getId());

            return userMapper.toDto(saved);
        } catch (Exception e) {
            log.error("⛔ Kullanıcı oluşturulurken hata oluştu:", e);
            throw e;
        }
    }

    @Override
    public void markUserAsDeleted(Long id) {
        log.info("Kullanıcı silme işareti atılıyor: ID {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Silme işlemi başarısız, kullanıcı bulunamadı: ID {}", id);
                    return new RuntimeException("Kullanıcı bulunamadı");
                });
        user.setHesapSilmeTarihi(LocalDateTime.now());
        userRepository.save(user);
        log.info("Kullanıcı silme işareti başarıyla atandı.");
    }

    @Override
    public void softDelete(Long id, String silinmeNedeni) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        user.setHesapSilmeTarihi(LocalDateTime.now());
        user.setSilinmeNedeni(silinmeNedeni);
        userRepository.save(user);
    }

    @Override
    public void unblockUser(Long id) {
        log.info("Bloke kaldırma işlemi: ID {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı: " + id));
        user.setBlokeMi(false);
        user.setSonBlokeTarihi(null);
        user.setHataliGirisSayisi(0);
        userRepository.save(user);
        log.info("Kullanıcı bloke durumu kaldırıldı: ID {}", id);
    }

    @Override
    public void resetPassword(Long id, String newRawPassword) {
        log.info("Şifre sıfırlama işlemi: ID {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı: " + id));

        // Yeni şifreyi encode edip atıyoruz
        user.setSifre(passwordEncoder.encode(newRawPassword));

        // Hatalı giriş sayısını ve bloke durumunu sıfırla
        user.setHataliGirisSayisi(0);
        user.setBlokeMi(false);
        user.setSonBlokeTarihi(null);

        userRepository.save(user);
        log.info("Kullanıcı şifresi başarıyla sıfırlandı: ID {}", id);
    }

    @Override
    public List<UserDTO> getUsersByFilter(UserFilterDTO filter) {
        List<User> users = userRepository.findAll().stream()
                .filter(u -> filter.getAd() == null || u.getAd().toLowerCase().contains(filter.getAd().toLowerCase()))
                .filter(u -> filter.getSicil() == null || u.getSicil().contains(filter.getSicil()))
                .filter(u -> filter.getUnvan() == null || u.getUnvan().equalsIgnoreCase(filter.getUnvan()))
                .filter(u -> filter.getUnite() == null ||
                        (u.getUnite() != null && u.getUnite().name().equalsIgnoreCase(filter.getUnite())))
                .filter(u -> filter.getRol() == null || u.getRol().name().equalsIgnoreCase(filter.getRol()))
                .filter(u -> filter.getBlokeMi() == null || u.isBlokeMi() == filter.getBlokeMi())
                .filter(u -> filter.getBaslangicTarihi() == null ||
                        (u.getHesapAcilisTarihi() != null && !u.getHesapAcilisTarihi().isBefore(filter.getBaslangicTarihi())))
                .filter(u -> filter.getBitisTarihi() == null ||
                        (u.getHesapAcilisTarihi() != null && !u.getHesapAcilisTarihi().isAfter(filter.getBitisTarihi())))
                .collect(Collectors.toList());

        return users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }
}