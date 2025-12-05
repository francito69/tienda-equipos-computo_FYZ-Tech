package com.fyztech.tienda.service;

import com.fyztech.tienda.model.dto.LoginRequest;
import com.fyztech.tienda.model.dto.LoginResponse;
import com.fyztech.tienda.model.entity.Usuario;
import com.fyztech.tienda.repository.UsuarioRepository;
import com.fyztech.tienda.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest loginRequest) {
        logger.info("🔐 INTENTANDO LOGIN - Email: {}", loginRequest.getEmail());
        
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getContraseña())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            Usuario usuario = usuarioRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            String jwt = jwtUtil.generateToken(loginRequest.getEmail());
            
            // LOG DE ÉXITO
            logger.info("✅ LOGIN EXITOSO - Usuario: {} | Rol: {} | Token generado", 
                       usuario.getEmail(), usuario.getRol());
            logger.info("📧 Datos usuario: {} {} | Email: {}", 
                       usuario.getNombres(), usuario.getApellidos(), usuario.getEmail());
            
            return new LoginResponse(jwt, usuario.getEmail(), usuario.getNombres(), usuario.getRol());
            
        } catch (Exception e) {
            // LOG DE ERROR
            logger.error("❌ LOGIN FALLIDO - Email: {} | Error: {}", 
                        loginRequest.getEmail(), e.getMessage());
            throw new RuntimeException("Credenciales inválidas");
        }
    }

    public Usuario registrar(Usuario usuario) {
        logger.info("👤 INTENTANDO REGISTRO - Email: {}", usuario.getEmail());
        
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            logger.error("❌ REGISTRO FALLIDO - Email ya existe: {}", usuario.getEmail());
            throw new RuntimeException("El email ya está registrado");
        }

        // Encriptar contraseña
        usuario.setContraseña(passwordEncoder.encode(usuario.getContraseña()));
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        // LOG DE ÉXITO
        logger.info("✅ REGISTRO EXITOSO - Usuario creado:");
        logger.info("   📧 Email: {}", usuarioGuardado.getEmail());
        logger.info("   👤 Nombres: {} {}", usuarioGuardado.getNombres(), usuarioGuardado.getApellidos());
        logger.info("   🎯 Rol: {}", usuarioGuardado.getRol());
        logger.info("   🔐 Contraseña: [ENCRIPTADA CON BCRYPT]");
        logger.info("   🆔 ID: {}", usuarioGuardado.getId());

        return usuarioGuardado;
    }
}