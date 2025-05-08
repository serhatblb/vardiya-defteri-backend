package com.kardemir.vardiyadefteri.service.impl;

import com.kardemir.vardiyadefteri.dto.DashboardDTO;
import com.kardemir.vardiyadefteri.entity.User;
import com.kardemir.vardiyadefteri.repository.UserRepository;
import com.kardemir.vardiyadefteri.repository.VardiyaRepository;
import com.kardemir.vardiyadefteri.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final VardiyaRepository vardiyaRepository;

    @Override
    public DashboardDTO getDashboardInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String sicil = auth.getName();
        User user = userRepository.findBySicil(sicil).orElseThrow();

        DashboardDTO dto = new DashboardDTO();

        // Bugünkü toplam not sayısı (herkes için ortak)
        dto.setTodayShifts(vardiyaRepository.countByTarih(LocalDate.now()));

        // Rol bazlı ekstra veriler
        switch (user.getRol()) {
            case SISTEM_YONETICISI -> {
                dto.setTotalUsers(userRepository.count());
                dto.setActiveUsers(userRepository.countByBlokeMiFalse());
                dto.setTotalShifts(vardiyaRepository.count());
            }
            case ISLETME_SORUMLUSU -> {
                dto.setTotalUsers(0); // kullanılmıyor ama null kalmasın
                dto.setActiveUsers(0);
                dto.setTotalShifts(vardiyaRepository.count()); // 👈 artık tüm notları görür
            }
            case NORMAL_KULLANICI -> {
                Long userId = user.getId();
                dto.setTotalUsers(0);
                dto.setActiveUsers(0);
                dto.setTotalShifts(vardiyaRepository.countByUserId(userId)); // sadece kendi notları
            }
        }

        return dto;
    }

}
