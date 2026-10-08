package com.chancla.chancla_lite_auth.repository;

import com.chancla.chancla_lite_auth.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    Optional<UsuarioEntity> findByEmail(String email);

    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.data.jpa.repository.Query("UPDATE UsuarioEntity u SET u.rol.id = :rolId WHERE u.id = :usuarioId")
    void actualizarRol(@org.springframework.data.repository.query.Param("usuarioId") Long usuarioId, @org.springframework.data.repository.query.Param("rolId") Short rolId);
}
