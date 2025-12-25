package com.kardemir.vardiyadefteri.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.BadCredentialsException;

import java.io.IOException;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {
        if (authException instanceof LockedException) {
            // 3. hatalı deneme sonucu LockedException fırlatıyoruz
            response.sendError(423, authException.getMessage());
        } else if (authException instanceof BadCredentialsException) {
            // normal hatalı parola
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Geçersiz sicil veya şifre");
        } else {
            // diğer tüm auth hataları
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed");
        }
    }
}
