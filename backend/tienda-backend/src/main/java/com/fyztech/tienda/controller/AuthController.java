package com.fyztech.tienda.controller;

import com.fyztech.tienda.model.dto.LoginRequest;
import com.fyztech.tienda.model.dto.LoginResponse;
import com.fyztech.tienda.model.entity.Usuario;
import com.fyztech.tienda.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        logger.info("🌐 SOLICITUD LOGIN recibida - Email: {}", loginRequest.getEmail());
        
        LoginResponse response = authService.login(loginRequest);
        
        logger.info("📤 RESPUESTA LOGIN enviada - Token generado para: {}", response.getEmail());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/registro")
    public ResponseEntity<Usuario> registro(@RequestBody Usuario usuario) {
        logger.info("🌐 SOLICITUD REGISTRO recibida - Email: {}", usuario.getEmail());
        
        Usuario usuarioRegistrado = authService.registrar(usuario);
        
        logger.info("📤 RESPUESTA REGISTRO enviada - Usuario creado: {}", usuarioRegistrado.getEmail());
        return ResponseEntity.ok(usuarioRegistrado);
    }
}