package com.chancla.chancla_lite_auth;

import com.chancla.chancla_lite_auth.service.LoginAttemptService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginAttemptServiceTest {

    private final LoginAttemptService service = new LoginAttemptService();

    @Test
    void noBloqueadoInicialmente() {
        assertFalse(service.isBlocked("ip|usuario@test.com"));
    }

    @Test
    void noBloqueaConMenosDeCincoFallos() {
        service.loginFailed("ip|usuario@test.com");
        service.loginFailed("ip|usuario@test.com");
        service.loginFailed("ip|usuario@test.com");
        service.loginFailed("ip|usuario@test.com");
        assertFalse(service.isBlocked("ip|usuario@test.com"));
    }

    @Test
    void bloqueaTrasCincoFallos() {
        for (int i = 0; i < 5; i++) {
            service.loginFailed("ip|usuario@test.com");
        }
        assertTrue(service.isBlocked("ip|usuario@test.com"));
    }

    @Test
    void loginExitosoLimpiaLosIntentos() {
        for (int i = 0; i < 5; i++) {
            service.loginFailed("ip|usuario@test.com");
        }
        assertTrue(service.isBlocked("ip|usuario@test.com"));
        service.loginSucceeded("ip|usuario@test.com");
        assertFalse(service.isBlocked("ip|usuario@test.com"));
    }

    @Test
    void elBloqueoEsPorCombinacionIpYEmail() {
        service.loginFailed("ipA|usuario@test.com");
        service.loginFailed("ipA|usuario@test.com");
        service.loginFailed("ipA|usuario@test.com");
        service.loginFailed("ipA|usuario@test.com");
        service.loginFailed("ipA|usuario@test.com");
        assertTrue(service.isBlocked("ipA|usuario@test.com"));
        assertFalse(service.isBlocked("ipB|usuario@test.com"));
        assertFalse(service.isBlocked("ipA|otro@test.com"));
    }

    @Test
    void nullNoGeneraErrores() {
        assertFalse(service.isBlocked(null));
        service.loginFailed(null);
        service.loginSucceeded(null);
        assertFalse(service.isBlocked(null));
    }
}
