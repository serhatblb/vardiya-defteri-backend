// src/main/java/com/kardemir/vardiyadefteri/service/impl/VardiyaServiceImpl.java
package com.kardemir.vardiyadefteri.service.impl;

import com.kardemir.vardiyadefteri.dto.VardiyaDTO;
import com.kardemir.vardiyadefteri.dto.VardiyaFilterDTO;
import com.kardemir.vardiyadefteri.entity.User;
import com.kardemir.vardiyadefteri.entity.Vardiya;
import com.kardemir.vardiyadefteri.repository.UserRepository;
import com.kardemir.vardiyadefteri.repository.VardiyaRepository;
import com.kardemir.vardiyadefteri.service.VardiyaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VardiyaServiceImpl implements VardiyaService {

    private final VardiyaRepository vardiyaRepository;
    private final UserRepository userRepository;

    @Override
    public VardiyaDTO ekleVardiya(Long userId, VardiyaDTO dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User aktif = userRepository.findBySicil(auth.getName())
                .orElseThrow(() -> new RuntimeException("Giriş yapan kullanıcı bulunamadı."));

        if ("NORMAL_KULLANICI".equals(aktif.getRol().name())) {
            throw new AccessDeniedException("NORMAL_KULLANICI rolü not ekleyemez.");
        }

        User hedef = (userId == null)
                ? aktif
                : userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı: " + userId));

        Vardiya v = Vardiya.builder()
                .user(hedef)
                .vardiyaType(dto.getVardiyaType())
                .tarih(dto.getTarih())
                .saat(LocalDateTime.now())
                .notIcerik(dto.getNotIcerik())
                .olusturmaTarihi(LocalDateTime.now())
                .build();

        try {
            return mapToDto(vardiyaRepository.save(v));
        } catch (DataIntegrityViolationException ex) {
            throw new RuntimeException("Aynı vardiya için zaten not var.", ex);
        }
    }

    @Override
    public VardiyaDTO getById(Long id) {
        return vardiyaRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Vardiya bulunamadı: " + id));
    }

    @Override
    public List<VardiyaDTO> getAll() {
        // eskiden tüm kayıtlar için kullanılıyordu, artık getVisible ile değişiyor
        return vardiyaRepository.findAll().stream()
                .filter(v -> v.getHesapSilmeTarihi() == null)
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VardiyaDTO> getAllForUser(Long userId) {
        return vardiyaRepository.findByUserId(userId).stream()
                .filter(v -> v.getHesapSilmeTarihi() == null)
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public VardiyaDTO guncelleVardiya(Long id, VardiyaDTO dto) {
        Vardiya vardiya = vardiyaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vardiya bulunamadı: " + id));

        vardiya.setTarih(dto.getTarih());
        vardiya.setVardiyaType(dto.getVardiyaType());
        vardiya.setSaat(LocalDateTime.now());
        vardiya.setNotIcerik(dto.getNotIcerik());

        // ✏️ Düzenleme zamanını kaydet
        vardiya.setGuncellemeTarihi(LocalDateTime.now());

        return mapToDto(vardiyaRepository.save(vardiya));
    }


    @Override
    public void silVardiya(Long id) {
        Vardiya v = vardiyaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vardiya bulunamadı: " + id));
        v.setHesapSilmeTarihi(LocalDateTime.now());
        vardiyaRepository.save(v);
    }

    @Override
    public List<VardiyaDTO> kullaniciyaGoreListele(Long userId) {
        return vardiyaRepository.findByUserIdAndHesapSilmeTarihiIsNull(userId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VardiyaDTO> getVisibleVardiyalarForLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User aktif = userRepository.findBySicil(auth.getName())
                .orElseThrow(() -> new RuntimeException("Giriş yapan kullanıcı bulunamadı."));

        // Eğer normal kullanıcıysa, sadece kendi ünitesine ait kayıtlar
        if ("NORMAL_KULLANICI".equals(aktif.getRol().name())) {
            String uni = aktif.getUnite().name();
            return vardiyaRepository.findAll().stream()
                    .filter(v -> v.getHesapSilmeTarihi() == null)
                    .filter(v -> v.getUser().getUnite() != null
                            && v.getUser().getUnite().name().equals(uni))
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
        }

        // Admin veya işletme yetkilisi tüm kayıtları görür
        return vardiyaRepository.findAll().stream()
                .filter(v -> v.getHesapSilmeTarihi() == null)
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    @Override
    public List<VardiyaDTO> getVardiyalarByFilter(VardiyaFilterDTO filter) {
        return vardiyaRepository.findAll().stream()
                .filter(v -> v.getHesapSilmeTarihi() == null)
                .filter(v -> filter.getVardiyaType() == null || v.getVardiyaType().equals(filter.getVardiyaType()))
                .filter(v -> filter.getUnite() == null || (v.getUser().getUnite() != null &&
                        v.getUser().getUnite().name().equalsIgnoreCase(filter.getUnite())))
                .filter(v -> filter.getYazarAdSoyad() == null ||
                        (v.getUser() != null &&
                                (v.getUser().getAd() + " " + v.getUser().getSoyad())
                                        .toLowerCase().contains(filter.getYazarAdSoyad().toLowerCase())))
                .filter(v -> filter.getBaslangicTarihi() == null ||
                        !v.getTarih().isBefore(filter.getBaslangicTarihi()))
                .filter(v -> filter.getBitisTarihi() == null ||
                        !v.getTarih().isAfter(filter.getBitisTarihi()))
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }



    private VardiyaDTO mapToDto(Vardiya v) {
        var user = v.getUser();
        String adSoyad = user.getAd() + " " + user.getSoyad();
        String uniteName = user.getUnite() != null ? user.getUnite().name() : null;

        return VardiyaDTO.builder()
                .id(v.getId())
                .userId(user.getId())
                .vardiyaType(v.getVardiyaType())
                .tarih(v.getTarih())
                .saat(v.getSaat())
                .notIcerik(v.getNotIcerik())
                .olusturmaTarihi(v.getOlusturmaTarihi())
                .guncellemeTarihi(v.getGuncellemeTarihi() != null ? v.getGuncellemeTarihi().toString() : null)

                .adSoyad(adSoyad)
                .unite(uniteName)
                .build();
    }
}
