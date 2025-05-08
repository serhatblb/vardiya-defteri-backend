package com.kardemir.vardiyadefteri.repository;

import com.kardemir.vardiyadefteri.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySicil(String sicil);
    boolean existsBySicil(String sicil);
    long countByBlokeMiFalse();
}
