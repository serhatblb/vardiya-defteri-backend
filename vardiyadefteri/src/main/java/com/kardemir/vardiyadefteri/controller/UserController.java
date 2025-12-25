package com.kardemir.vardiyadefteri.controller;

import com.kardemir.vardiyadefteri.dto.UserDTO;
import com.kardemir.vardiyadefteri.dto.UserFilterDTO;
import com.kardemir.vardiyadefteri.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 1) Tüm kullanıcıları listele
    @PreAuthorize("hasRole('SISTEM_YONETICISI')")
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // 2) Tek kullanıcı getir
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // 3) Yeni kullanıcı oluştur
    @PostMapping
    public ResponseEntity<UserDTO> create(@RequestBody UserDTO dto) {
        UserDTO created = userService.createUser(dto);
        return ResponseEntity.ok(created);
    }

    // 4) Kullanıcıya silme işareti at (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.markUserAsDeleted(id);
        return ResponseEntity.noContent().build();
    }

    // 5) Bloke kaldırma
    @PreAuthorize("hasRole('SISTEM_YONETICISI')")
    @PatchMapping("/{id}/unblock")
    public ResponseEntity<Void> unblock(@PathVariable Long id) {
        userService.unblockUser(id);
        return ResponseEntity.noContent().build();
    }

    // 6) Soft delete with reason
    @PutMapping("/delete/{id}")
    public ResponseEntity<String> softDeleteUser(
            @PathVariable Long id,
            @RequestParam String silinmeNedeni
    ) {
        userService.softDelete(id, silinmeNedeni);
        return ResponseEntity.ok("Kullanıcı başarıyla silindi.");
    }

    // 7) Şifre Sıfırlama
    @PreAuthorize("hasRole('SISTEM_YONETICISI')")
    @PostMapping("/{id}/reset-password")
    public ResponseEntity<Void> resetPassword(
            @PathVariable Long id,
            @RequestBody Map<String, String> body
    ) {
        String newPassword = body.get("newPassword");
        if (newPassword == null || newPassword.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        userService.resetPassword(id, newPassword);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/rapor")
    @PreAuthorize("hasRole('SISTEM_YONETICISI')")
    public ResponseEntity<List<UserDTO>> getFilteredUsers(@RequestBody UserFilterDTO filter) {
        return ResponseEntity.ok(userService.getUsersByFilter(filter));
    }


}
