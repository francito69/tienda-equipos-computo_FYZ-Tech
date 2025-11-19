package com.fyztech.tienda.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ordenes")
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "monto_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoTotal;

    @Column(nullable = false)
    private String estado = "PENDIENTE";

    @Column(name = "direccion_envio", columnDefinition = "JSONB")
    private String direccionEnvio;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrdenItem> items = new ArrayList<>();

    @OneToOne(mappedBy = "orden", cascade = CascadeType.ALL)
    private Pago pago;

    public Orden() {}

    public Orden(Usuario usuario, String direccionEnvio) {
        this.usuario = usuario;
        this.direccionEnvio = direccionEnvio;
        this.montoTotal = BigDecimal.ZERO;
    }

    public void agregarItem(OrdenItem item) {
        items.add(item);
        item.setOrden(this);
        calcularMontoTotal();
    }

    public void removerItem(OrdenItem item) {
        items.remove(item);
        item.setOrden(null);
        calcularMontoTotal();
    }

    private void calcularMontoTotal() {
        this.montoTotal = items.stream()
                .map(OrdenItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Getters y Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public BigDecimal getMontoTotal() { return montoTotal; }
    public void setMontoTotal(BigDecimal montoTotal) { this.montoTotal = montoTotal; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getDireccionEnvio() { return direccionEnvio; }
    public void setDireccionEnvio(String direccionEnvio) { this.direccionEnvio = direccionEnvio; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public List<OrdenItem> getItems() { return items; }
    public void setItems(List<OrdenItem> items) { 
        this.items = items;
        calcularMontoTotal();
    }

    public Pago getPago() { return pago; }
    public void setPago(Pago pago) { this.pago = pago; }
}