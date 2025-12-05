package com.fyztech.tienda.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pagos")
public class Pago {
    
    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_id")
    private Orden orden;

    @Column(name = "metodo_pago")
    private String metodoPago;

    private BigDecimal monto;
    
    private String estado;

    // USAR LOS NOMBRES EXISTENTES EN TU BD
    @Column(name = "qr_code_url")  // ← YA EXISTE en tu BD
    private String qrImageName;

    @Column(name = "comprobante_url")  // ← YA EXISTE en tu BD  
    private String comprobanteImageName;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    // Constructores
    public Pago() {}

    public Pago(Orden orden, String metodoPago) {
        this.orden = orden;
        this.metodoPago = metodoPago;
        this.monto = orden.getMontoTotal();
        this.estado = "PENDIENTE";
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    // Getters y Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Orden getOrden() { return orden; }
    public void setOrden(Orden orden) { this.orden = orden; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    // CAMBIOS AQUÍ: Usar los nombres de BD existentes
    public String getQrImageName() { return qrImageName; }
    public void setQrImageName(String qrImageName) { this.qrImageName = qrImageName; }

    public String getComprobanteImageName() { return comprobanteImageName; }
    public void setComprobanteImageName(String comprobanteImageName) { this.comprobanteImageName = comprobanteImageName; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}