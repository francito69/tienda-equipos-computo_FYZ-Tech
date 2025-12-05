// backend/src/main/java/com/fyztech/tienda/model/dto/OrdenRequestDTO.java
package com.fyztech.tienda.model.dto;

public class OrdenRequestDTO {
    private String direccionEnvio;
    private String metodoPago;

    public OrdenRequestDTO() {}

    public OrdenRequestDTO(String direccionEnvio, String metodoPago) {
        this.direccionEnvio = direccionEnvio;
        this.metodoPago = metodoPago;
    }

    // Getters y Setters
    public String getDireccionEnvio() { return direccionEnvio; }
    public void setDireccionEnvio(String direccionEnvio) { this.direccionEnvio = direccionEnvio; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
}
