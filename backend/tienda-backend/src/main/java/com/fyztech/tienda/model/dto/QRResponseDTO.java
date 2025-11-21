// backend/src/main/java/com/fyztech/tienda/model/dto/QRResponseDTO.java
package com.fyztech.tienda.model.dto;

import java.math.BigDecimal;

public class QRResponseDTO {
    private String qrImageUrl;
    private BigDecimal monto;
    private String concepto;
    private String numeroCelular;
    private String instrucciones;

    // Constructores
    public QRResponseDTO() {}

    public QRResponseDTO(String qrImageUrl, BigDecimal monto, String concepto, 
                        String numeroCelular, String instrucciones) {
        this.qrImageUrl = qrImageUrl;
        this.monto = monto;
        this.concepto = concepto;
        this.numeroCelular = numeroCelular;
        this.instrucciones = instrucciones;
    }

    // Getters y Setters
    public String getQrImageUrl() { return qrImageUrl; }
    public void setQrImageUrl(String qrImageUrl) { this.qrImageUrl = qrImageUrl; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public String getConcepto() { return concepto; }
    public void setConcepto(String concepto) { this.concepto = concepto; }

    public String getNumeroCelular() { return numeroCelular; }
    public void setNumeroCelular(String numeroCelular) { this.numeroCelular = numeroCelular; }

    public String getInstrucciones() { return instrucciones; }
    public void setInstrucciones(String instrucciones) { this.instrucciones = instrucciones; }
}