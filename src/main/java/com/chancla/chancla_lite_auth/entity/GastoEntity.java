package com.chancla.chancla_lite_auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "gastos")
@AllArgsConstructor
@NoArgsConstructor
public class GastoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sesion_id", nullable = false)
    private SesionTrabajoEntity sesion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_gasto_id", nullable = false)
    private CategoriaGastoEntity categoriaGasto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategoria_gasto_id", nullable = true)
    private SubcategoriaGastoEntity subcategoriaGasto;

    @Column(nullable = false, length = 200)
    private String descripcion;

    @Column(nullable = false)
    private Double monto;

    @org.hibernate.annotations.CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "fecha_registro_manual")
    private LocalDateTime fechaRegistroManual;

    @org.hibernate.annotations.UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
