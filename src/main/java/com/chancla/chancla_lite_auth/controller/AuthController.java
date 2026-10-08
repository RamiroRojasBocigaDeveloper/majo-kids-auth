package com.chancla.chancla_lite_auth.controller;

import com.chancla.chancla_lite_auth.config.JwtTokenProvider;
import com.chancla.chancla_lite_auth.dto.AuthResponse;
import com.chancla.chancla_lite_auth.dto.LoginRequest;
import com.chancla.chancla_lite_auth.entity.UsuarioEntity;
import com.chancla.chancla_lite_auth.repository.UsuarioRepository;
import com.chancla.chancla_lite_auth.service.LoginAttemptService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final LoginAttemptService loginAttemptService;

    public AuthController(UsuarioRepository usuarioRepository,
                          JwtTokenProvider jwtTokenProvider,
                          PasswordEncoder passwordEncoder,
                          LoginAttemptService loginAttemptService) {
        this.usuarioRepository = usuarioRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.loginAttemptService = loginAttemptService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        String key = claveIntento(request.getRemoteAddr(), loginRequest.getEmail());

        if (loginAttemptService.isBlocked(key)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(Map.of("message", "Demasiados intentos fallidos. Intenta de nuevo en unos minutos."));
        }

        UsuarioEntity usuario = usuarioRepository.findByEmail(loginRequest.getEmail())
                .orElse(null);

        if (usuario != null && usuario.getActivo()
                && passwordEncoder.matches(loginRequest.getPassword(), usuario.getPassword())) {
            loginAttemptService.loginSucceeded(key);
            String token = jwtTokenProvider.generarToken(usuario.getEmail());
            String rolNombre = usuario.getRol() != null ? usuario.getRol().getNombre() : "SIN_ROL";
            return ResponseEntity.ok(new AuthResponse(token, usuario.getId(), usuario.getEmail(), usuario.getNombre(), rolNombre));
        }

        loginAttemptService.loginFailed(key);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    private String claveIntento(String ip, String email) {
        return ip + "|" + (email == null ? "" : email.toLowerCase());
    }
}
