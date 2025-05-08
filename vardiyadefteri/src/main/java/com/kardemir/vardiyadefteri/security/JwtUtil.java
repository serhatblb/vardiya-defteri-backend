package com.kardemir.vardiyadefteri.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.annotation.PostConstruct;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${app.jwt.secret}")
    private String secretBase64;

    @Value("${app.jwt.expiration-ms}")
    private long expMs;

    private Key key;

    @PostConstruct
    public void init() {
        byte[] secretBytes = java.util.Base64.getDecoder().decode(secretBase64);
        this.key = Keys.hmacShaKeyFor(secretBytes);
    }

    public String generateToken(UserDetails userDetails) {
        Long userId = ((CustomUserDetails) userDetails).getUser().getId();

        // Tekil rolü ROLE_ prefix'iyle ekliyoruz
        String rol = userDetails.getAuthorities().stream()
                .map(a -> a.getAuthority()) // Örn: "ROLE_NORMAL_KULLANICI"
                .findFirst()
                .orElse("");

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("id", userId)
                .claim("roles", rol) // <-- "rol" değil, "roles" ve prefixli
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            Claims claims = parseClaims(token);
            return claims.getSubject().equals(userDetails.getUsername())
                    && !claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public String getUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public String getRole(String token) {
        return parseClaims(token).get("roles", String.class); // rol → roles
    }


    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
