package com.chancla.chancla_lite_auth.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationMillis}")
    private long jwtExpirationMillis;

    public String generarToken(String email) {
        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + jwtExpirationMillis);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(ahora)
                .setExpiration(expiracion)
                .signWith(getFirmaKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String obtenerEmailDeToken(String token) {
        Claims claims = obtenerClaims(token);
        return claims.getSubject();
    }

    /**
     * Valida la firma y la expiración en una sola pasada.
     * Devuelve el email (subject) si el token es válido, o null en caso contrario.
     */
    public String obtenerEmailSiValido(String token) {
        try {
            return obtenerEmailDeToken(token);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean validarToken(String token) {
        try {
            obtenerClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims obtenerClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getFirmaKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getFirmaKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
}
