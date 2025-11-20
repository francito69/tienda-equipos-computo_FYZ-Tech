// backend/src/main/java/com/fyztech/tienda/model/dto/LoginResponse.java
package com.fyztech.tienda.model.dto;

public class LoginResponse {
    private String token;
    private String tipo = "Bearer";
    private String email;
    private String nombres;
    private String rol;

    // Constructores
    public LoginResponse() {}
    public LoginResponse(String token, String email, String nombres, String rol) {
        this.token = token;
        this.email = email;
        this.nombres = nombres;
        this.rol = rol;
    }

    // Getters y Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}