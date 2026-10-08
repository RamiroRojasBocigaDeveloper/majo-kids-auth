package com.chancla.chancla_lite_auth;

import com.chancla.chancla_lite_auth.config.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtTokenProviderTest {

    private static final String SECRET = "clave-secreta-de-prueba-que-supera-los-256-bits-seguros-para-hs256";
    private static final long EXPIRATION_MILLIS = 86400000L;

    private final JwtTokenProvider provider = new JwtTokenProvider();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(provider, "jwtSecret", SECRET);
        ReflectionTestUtils.setField(provider, "jwtExpirationMillis", EXPIRATION_MILLIS);
    }

    @Test
    void generaYLeeTokenValido() {
        String token = provider.generarToken("vendedor@test.com");
        assertNotNull(token);
        assertTrue(provider.validarToken(token));
        assertEquals("vendedor@test.com", provider.obtenerEmailDeToken(token));
        assertEquals("vendedor@test.com", provider.obtenerEmailSiValido(token));
    }

    @Test
    void rechazaTokenManipulado() {
        String token = provider.generarToken("vendedor@test.com");
        String manipulado = token.substring(0, token.length() - 2) + "XX";
        assertFalse(provider.validarToken(manipulado));
        assertNull(provider.obtenerEmailSiValido(manipulado));
    }

    @Test
    void rechazaTokenFirmadoConOtraClave() {
        JwtTokenProvider otro = new JwtTokenProvider();
        ReflectionTestUtils.setField(otro, "jwtSecret", "otra-clave-distinta-pero-igualmente-larga-para-firmar-hs256");
        ReflectionTestUtils.setField(otro, "jwtExpirationMillis", EXPIRATION_MILLIS);
        String tokenOtro = otro.generarToken("vendedor@test.com");
        assertFalse(provider.validarToken(tokenOtro));
    }

    @Test
    void rechazaTokenVacioONull() {
        assertFalse(provider.validarToken(""));
        assertFalse(provider.validarToken(null));
        assertFalse(provider.validarToken("texto-sin-formato-jwt"));
        assertNull(provider.obtenerEmailSiValido("token-invalido"));
    }
}
