// src/main/java/com/kardemir/vardiyadefteri/controller/VardiyaController.java
package com.kardemir.vardiyadefteri.controller;

import com.kardemir.vardiyadefteri.dto.VardiyaDTO;
import com.kardemir.vardiyadefteri.dto.VardiyaFilterDTO;
import com.kardemir.vardiyadefteri.service.VardiyaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vardiyas")
@RequiredArgsConstructor
public class VardiyaController {
    private final VardiyaService vardiyaService;

    // Yeni not ekleme: sadece admin & işletme yetkilisi
    @PreAuthorize("hasAnyRole('SISTEM_YONETICISI','ISLETME_SORUMLUSU')")
    @PostMapping("/{userId}")
    public ResponseEntity<VardiyaDTO> ekle(
            @PathVariable Long userId,
            @Valid @RequestBody VardiyaDTO dto
    ) {
        return ResponseEntity.ok(vardiyaService.ekleVardiya(userId, dto));
    }

    // 🌟 Bu endpoint ile login kullanıcıya göre doğru notları döner
    @PreAuthorize("hasAnyRole('SISTEM_YONETICISI','ISLETME_SORUMLUSU','NORMAL_KULLANICI')")
    @GetMapping
    public ResponseEntity<List<VardiyaDTO>> getVisibleNotlar() {
        var liste = vardiyaService.getVisibleVardiyalarForLoggedInUser();
        return ResponseEntity.ok(liste);
    }

    // Belirli bir kullanıcının tüm notları (admin/işletme yetkisi veya kendi notları için)
    @PreAuthorize("hasAnyRole('SISTEM_YONETICISI','ISLETME_SORUMLUSU','NORMAL_KULLANICI')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<VardiyaDTO>> getAllForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(vardiyaService.getAllForUser(userId));
    }
    @PreAuthorize("hasAnyRole('SISTEM_YONETICISI', 'ISLETME_SORUMLUSU', 'NORMAL_KULLANICI')")
    @PostMapping("/rapor")
    public ResponseEntity<List<VardiyaDTO>> raporla(@RequestBody VardiyaFilterDTO filter) {
        return ResponseEntity.ok(vardiyaService.getVardiyalarByFilter(filter));
    }

    // Güncelleme: admin & işletme
    @PreAuthorize("hasAnyRole('SISTEM_YONETICISI','ISLETME_SORUMLUSU')")
    @PutMapping("/{id}")
    public ResponseEntity<VardiyaDTO> guncelle(
            @PathVariable Long id,
            @Valid @RequestBody VardiyaDTO dto
    ) {
        return ResponseEntity.ok(vardiyaService.guncelleVardiya(id, dto));
    }

    // Silme: sadece admin
    @PreAuthorize("hasRole('SISTEM_YONETICISI')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> sil(@PathVariable Long id) {
        vardiyaService.silVardiya(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('SISTEM_YONETICISI','ISLETME_SORUMLUSU')")
    @GetMapping("/{id}")
    public ResponseEntity<VardiyaDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(vardiyaService.getById(id));
    }

}
