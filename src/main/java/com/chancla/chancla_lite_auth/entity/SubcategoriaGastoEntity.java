package com.chancla.chancla_lite_auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "subcategorias_gastos")
@AllArgsConstructor
@NoArgsConstructor
public class SubcategoriaGastoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_gasto_id", nullable = false, columnDefinition = "INT UNSIGNED")
    private CategoriaGastoEntity categoriaGasto;

    @Column(nullable = false, length = 50)
    private String nombre;

    @org.hibernate.annotations.CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
