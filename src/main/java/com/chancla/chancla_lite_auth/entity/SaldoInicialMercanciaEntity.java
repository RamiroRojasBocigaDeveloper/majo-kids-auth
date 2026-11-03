package com.chancla.chancla_lite_auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "saldos_iniciales_mercancia")
@AllArgsConstructor
@NoArgsConstructor
public class SaldoInicialMercanciaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private ProductoEntity producto;

    @Column(name = "cantidad_base", nullable = false)
    private Integer cantidadBase;

    @Column(name = "monto_base", nullable = false)
    private Double montoBase;

    @Column(name = "fecha_corte", nullable = false)
    private LocalDate fechaCorte;

    @org.hibernate.annotations.CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
