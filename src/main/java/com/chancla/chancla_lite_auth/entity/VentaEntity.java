package com.chancla.chancla_lite_auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ventas")
@AllArgsConstructor
@NoArgsConstructor
public class VentaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_factura", nullable = false, unique = true, length = 30)
    private String numeroFactura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sesion_id", nullable = false)
    private SesionTrabajoEntity sesion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "metodo_pago_id", nullable = false)
    private MetodoPagoEntity metodoPago;

    @Column(nullable = false)
    private Double subtotal;

    @Column(nullable = false)
    private Double descuento = 0.0;

    @Column(nullable = false)
    private Double total;

    @org.hibernate.annotations.CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "fecha_registro_manual")
    private LocalDateTime fechaRegistroManual;

    @org.hibernate.annotations.UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<DetalleVentaEntity> detalles;
}
