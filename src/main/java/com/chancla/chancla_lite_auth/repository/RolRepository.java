package com.chancla.chancla_lite_auth.repository;

import com.chancla.chancla_lite_auth.entity.RolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<RolEntity, Short> {
    Optional<RolEntity> findByNombre(String nombre);
}
