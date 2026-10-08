package com.chancla.chancla_lite_auth.repository;

import com.chancla.chancla_lite_auth.entity.AuditoriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditoriaRepository extends JpaRepository<AuditoriaEntity, Long> {
}
