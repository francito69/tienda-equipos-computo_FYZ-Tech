// backend/src/main/java/com/fyztech/tienda/model/dto/CarritoResponseDTO.java
package com.fyztech.tienda.model.dto;

import java.math.BigDecimal;
import java.util.List;

public class CarritoResponseDTO {
    private List<CarritoItemDTO> items;
    private BigDecimal total;
    private Integer totalItems;

    public CarritoResponseDTO() {}

    public CarritoResponseDTO(List<CarritoItemDTO> items, BigDecimal total, Integer totalItems) {
        this.items = items;
        this.total = total;
        this.totalItems = totalItems;
    }

    // Getters y Setters
    public List<CarritoItemDTO> getItems() { return items; }
    public void setItems(List<CarritoItemDTO> items) { this.items = items; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public Integer getTotalItems() { return totalItems; }
    public void setTotalItems(Integer totalItems) { this.totalItems = totalItems; }
}