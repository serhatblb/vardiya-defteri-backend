// src/main/java/com/kardemir/vardiyadefteri/service/VardiyaService.java
package com.kardemir.vardiyadefteri.service;

import com.kardemir.vardiyadefteri.dto.VardiyaDTO;
import com.kardemir.vardiyadefteri.dto.VardiyaFilterDTO;

import java.util.List;

public interface VardiyaService {
    VardiyaDTO ekleVardiya(Long userId, VardiyaDTO dto);
    VardiyaDTO getById(Long id);
    List<VardiyaDTO> getAll();
    List<VardiyaDTO> getAllForUser(Long userId);
    VardiyaDTO guncelleVardiya(Long id, VardiyaDTO dto);
    List<VardiyaDTO> kullaniciyaGoreListele(Long userId);
    /** Yeni: login olan kullanıcıya göre görünür notları döner */
    List<VardiyaDTO> getVisibleVardiyalarForLoggedInUser();
    List<VardiyaDTO> getVardiyalarByFilter(VardiyaFilterDTO filter);

    void silVardiya(Long id);
}
