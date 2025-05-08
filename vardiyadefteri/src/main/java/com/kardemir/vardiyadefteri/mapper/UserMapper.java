package com.kardemir.vardiyadefteri.mapper;

import com.kardemir.vardiyadefteri.dto.UserDTO;
import com.kardemir.vardiyadefteri.entity.User;
import com.kardemir.vardiyadefteri.entity.Unite;
import com.kardemir.vardiyadefteri.entity.Rol;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {

    public UserDTO toDto(User user) {
        if (user == null) {
            return null;
        }

        return UserDTO.builder()
                .id(user.getId())
                .sicil(user.getSicil())
                .ad(user.getAd())
                .soyad(user.getSoyad())
                .unvan(user.getUnvan())
                .unite(user.getUnite() != null ? user.getUnite().name() : null)
                .rol(user.getRol() != null ? user.getRol().name() : null)
                .hesapAcilisTarihi(user.getHesapAcilisTarihi())
                .hesapSilmeTarihi(user.getHesapSilmeTarihi())
                .silinmeNedeni(user.getSilinmeNedeni())
                .blokeMi(user.isBlokeMi())
                .sonBlokeTarihi(user.getSonBlokeTarihi())
                .build();
    }

    public User toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }

        return User.builder()
                .sicil(dto.getSicil())
                .ad(dto.getAd())
                .soyad(dto.getSoyad())
                .unvan(dto.getUnvan())
                .unite(dto.getUnite() != null ? Unite.valueOf(dto.getUnite()) : null)
                .rol(dto.getRol() != null ? Rol.valueOf(dto.getRol()) : null)
                .silinmeNedeni(dto.getSilinmeNedeni())
                .build();
    }

    public List<UserDTO> toDtoList(List<User> users) {
        List<UserDTO> list = new ArrayList<>();
        if (users != null) {
            for (User u : users) {
                list.add(toDto(u));
            }
        }
        return list;
    }
}
