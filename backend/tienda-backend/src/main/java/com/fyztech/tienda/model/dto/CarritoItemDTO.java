// backend/src/main/java/com/fyztech/tienda/model/dto/CarritoItemDTO.java
package com.fyztech.tienda.model.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class CarritoItemDTO {
    private UUID productoId;
    private String productoNombre;
    private BigDecimal precioUnitario;
    private Integer cantidad;
    private String imagenUrl;

    public CarritoItemDTO() {}

    public CarritoItemDTO(UUID productoId, String productoNombre, BigDecimal precioUnitario, 
                         Integer cantidad, String imagenUrl) {
        this.productoId = productoId;
        this.productoNombre = productoNombre;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
        this.imagenUrl = imagenUrl;
    }

    // Getters y Setters
    public UUID getProductoId() { return productoId; }
    public void setProductoId(UUID productoId) { this.productoId = productoId; }

    public String getProductoNombre() { return productoNombre; }
    public void setProductoNombre(String productoNombre) { this.productoNombre = productoNombre; }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public BigDecimal getSubtotal() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }
}