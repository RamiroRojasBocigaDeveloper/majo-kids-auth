package com.chancla.chancla_lite_auth.entity;

import com.chancla.chancla_lite_auth.enums.MetodoPagoCompra;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "compras_mercancia")
@AllArgsConstructor
@NoArgsConstructor
public class CompraMercanciaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(length = 150)
    private String proveedor;

    @Column(length = 200)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", length = 20)
    private MetodoPagoCompra metodoPago;

    @Column(name = "monto_total", nullable = false)
    private Double montoTotal;

    @Column(nullable = false)
    private Boolean detallada = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private UsuarioEntity usuario;

    @org.hibernate.annotations.CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
