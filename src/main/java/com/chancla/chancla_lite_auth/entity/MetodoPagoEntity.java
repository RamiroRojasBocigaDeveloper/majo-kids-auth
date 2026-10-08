package com.chancla.chancla_lite_auth.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "metodos_pago")
@AllArgsConstructor
@NoArgsConstructor
public class MetodoPagoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre del metodo de pago es obligatorio")
    @Size(max = 30, message = "El nombre no puede exceder 30 caracteres")
    @Column(nullable = false, unique = true, length = 30)
    private String nombre;

    @org.hibernate.annotations.CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
