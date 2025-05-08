package com.kardemir.vardiyadefteri.security;

import com.kardemir.vardiyadefteri.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {
    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Spring Security requires ROLE_ prefix for hasRole checks
        String roleName = user.getRol().name();
        return List.of(new SimpleGrantedAuthority("ROLE_" + roleName));
    }

    @Override
    public String getPassword() {
        return user.getSifre();
    }

    @Override
    public String getUsername() {
        return user.getSicil();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.isBlokeMi();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getHesapSilmeTarihi() == null;
    }

    // Yardımcı metotlar
    public Long getUserId() {
        return user.getId();
    }

    public String getRoleName() {
        return user.getRol().name();
    }
}
