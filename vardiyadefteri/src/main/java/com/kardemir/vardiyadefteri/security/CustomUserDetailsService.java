package com.kardemir.vardiyadefteri.security;

import com.kardemir.vardiyadefteri.entity.User;
import com.kardemir.vardiyadefteri.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String sicil) throws UsernameNotFoundException {
        User user = userRepository.findBySicil(sicil)
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı: " + sicil));
        return new CustomUserDetails(user);
    }
}
