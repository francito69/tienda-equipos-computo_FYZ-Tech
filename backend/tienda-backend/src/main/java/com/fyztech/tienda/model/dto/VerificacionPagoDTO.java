// backend/src/main/java/com/fyztech/tienda/model/dto/VerificacionPagoDTO.java
package com.fyztech.tienda.model.dto;

public class VerificacionPagoDTO {
    private boolean aprobado;
    private String observaciones;

    public VerificacionPagoDTO() {}

    public VerificacionPagoDTO(boolean aprobado, String observaciones) {
        this.aprobado = aprobado;
        this.observaciones = observaciones;
    }

    // Getters y Setters
    public boolean isAprobado() { return aprobado; }
    public void setAprobado(boolean aprobado) { this.aprobado = aprobado; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}