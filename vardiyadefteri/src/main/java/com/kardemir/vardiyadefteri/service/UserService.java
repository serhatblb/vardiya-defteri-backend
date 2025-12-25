package com.kardemir.vardiyadefteri.service;

import com.kardemir.vardiyadefteri.dto.UserDTO;
import com.kardemir.vardiyadefteri.dto.UserFilterDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUsers();
    List<UserDTO> getUsersByFilter(UserFilterDTO filter);

    UserDTO getUserById(Long id);
    UserDTO createUser(UserDTO dto);
    void markUserAsDeleted(Long id);
    void unblockUser(Long id);
    void softDelete(Long id, String silinmeNedeni);

    // ← Yeni metod
    void resetPassword(Long id, String newRawPassword);

}
