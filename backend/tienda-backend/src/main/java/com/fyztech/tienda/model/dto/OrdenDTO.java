// backend/src/main/java/com/fyztech/tienda/model/dto/OrdenDTO.java
package com.fyztech.tienda.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class OrdenDTO {
    private UUID id;
    private BigDecimal montoTotal;
    private String estado;
    private String direccionEnvio;
    private LocalDateTime fechaCreacion;
    private List<OrdenItemDTO> items;

    public OrdenDTO() {}

    public OrdenDTO(UUID id, BigDecimal montoTotal, String estado, String direccionEnvio, 
                   LocalDateTime fechaCreacion, List<OrdenItemDTO> items) {
        this.id = id;
        this.montoTotal = montoTotal;
        this.estado = estado;
        this.direccionEnvio = direccionEnvio;
        this.fechaCreacion = fechaCreacion;
        this.items = items;
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

    public List<OrdenItemDTO> getItems() { return items; }
    public void setItems(List<OrdenItemDTO> items) { this.items = items; }
}