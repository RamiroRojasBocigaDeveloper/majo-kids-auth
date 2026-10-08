package com.chancla.chancla_lite_auth.config;

import com.chancla.chancla_lite_auth.entity.RolEntity;
import com.chancla.chancla_lite_auth.entity.UsuarioEntity;
import com.chancla.chancla_lite_auth.enums.RolNombre;
import com.chancla.chancla_lite_auth.repository.RolRepository;
import com.chancla.chancla_lite_auth.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Crea datos de arranque SOLO en el perfil dev. Nunca se ejecuta en prod.
 */
@Component
@Profile("dev")
@RequiredArgsConstructor
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        try {
            // 1. Crear roles básicos si no existen (match sin distinguir mayúsculas)
            crearRolSiNoExiste(RolNombre.ADMINISTRADOR);
            crearRolSiNoExiste(RolNombre.VENDEDOR);
            crearRolSiNoExiste(RolNombre.JEFE);

            // 2. Crear usuario admin inicial (solo si no existe)
            String adminEmail = "admin@majokids.com";
            if (usuarioRepository.findByEmail(adminEmail).isEmpty()) {
                RolEntity adminRol = buscarRol(RolNombre.ADMINISTRADOR)
                        .orElseThrow(() -> new RuntimeException("Rol ADMINISTRADOR no encontrado"));

                UsuarioEntity admin = new UsuarioEntity();
                admin.setNombre("Administrador MajoKids");
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRol(adminRol);
                admin.setActivo(true);
                admin.setSueldoDiario(0.0);

                usuarioRepository.save(admin);
                log.debug("Admin creado correctamente");
            }

            // 3. Crear usuario Rami Test (solo dev)
            String ramiEmail = "rami@tienda.com";
            if (usuarioRepository.findByEmail(ramiEmail).isEmpty()) {
                RolEntity adminRol = buscarRol(RolNombre.ADMINISTRADOR)
                        .orElseThrow(() -> new RuntimeException("Rol ADMINISTRADOR no encontrado"));

                UsuarioEntity rami = new UsuarioEntity();
                rami.setNombre("Rami Test");
                rami.setEmail(ramiEmail);
                rami.setPassword(passwordEncoder.encode("admin123"));
                rami.setRol(adminRol);
                rami.setActivo(true);
                rami.setSueldoDiario(0.0);

                usuarioRepository.save(rami);
                log.debug("Rami Test creado correctamente");
            }
        } catch (Exception e) {
            log.error("Error en DataInitializer: {}", e.getMessage());
        }
    }

    private void crearRolSiNoExiste(RolNombre rolNombre) {
        if (buscarRol(rolNombre).isEmpty()) {
            RolEntity rol = new RolEntity();
            rol.setNombre(rolNombre.name());
            rolRepository.save(rol);
        }
    }

    private java.util.Optional<RolEntity> buscarRol(RolNombre rolNombre) {
        List<RolEntity> roles = rolRepository.findAll();
        return roles.stream()
                .filter(r -> r.getNombre() != null && r.getNombre().equalsIgnoreCase(rolNombre.name()))
                .findFirst();
    }
}
