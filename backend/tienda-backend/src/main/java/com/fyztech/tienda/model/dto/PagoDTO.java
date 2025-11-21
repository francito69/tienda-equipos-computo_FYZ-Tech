// backend/src/main/java/com/fyztech/tienda/model/dto/PagoDTO.java
package com.fyztech.tienda.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class PagoDTO {
    private UUID id;
    private String metodoPago;
    private BigDecimal monto;
    private String estado;
    private String qrImageUrl;
    private String comprobanteImageUrl;
    private LocalDateTime fechaCreacion;
    private UUID ordenId;
    private String ordenEstado;

    // Constructores
    public PagoDTO() {}

    public PagoDTO(UUID id, String metodoPago, BigDecimal monto, String estado, 
                  String qrImageUrl, String comprobanteImageUrl, LocalDateTime fechaCreacion,
                  UUID ordenId, String ordenEstado) {
        this.id = id;
        this.metodoPago = metodoPago;
        this.monto = monto;
        this.estado = estado;
        this.qrImageUrl = qrImageUrl;
        this.comprobanteImageUrl = comprobanteImageUrl;
        this.fechaCreacion = fechaCreacion;
        this.ordenId = ordenId;
        this.ordenEstado = ordenEstado;
    }

    // Getters y Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getQrImageUrl() { return qrImageUrl; }
    public void setQrImageUrl(String qrImageUrl) { this.qrImageUrl = qrImageUrl; }

    public String getComprobanteImageUrl() { return comprobanteImageUrl; }
    public void setComprobanteImageUrl(String comprobanteImageUrl) { this.comprobanteImageUrl = comprobanteImageUrl; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public UUID getOrdenId() { return ordenId; }
    public void setOrdenId(UUID ordenId) { this.ordenId = ordenId; }

    public String getOrdenEstado() { return ordenEstado; }
    public void setOrdenEstado(String ordenEstado) { this.ordenEstado = ordenEstado; }
}