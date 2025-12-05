// backend/src/main/java/com/fyztech/tienda/model/dto/LoginRequest.java
package com.fyztech.tienda.model.dto;

public class LoginRequest {
    private String email;
    private String contraseña;

    // Constructores
    public LoginRequest() {}
    public LoginRequest(String email, String contraseña) {
        this.email = email;
        this.contraseña = contraseña;
    }

    // Getters y Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getContraseña() { return contraseña; }
    public void setContraseña(String contraseña) { this.contraseña = contraseña; }
}