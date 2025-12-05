// backend/src/main/java/com/fyztech/tienda/controller/PagoController.java
package com.fyztech.tienda.controller;

import com.fyztech.tienda.model.dto.PagoDTO;
import com.fyztech.tienda.model.dto.QRResponseDTO;
import com.fyztech.tienda.model.dto.VerificacionPagoDTO;
import com.fyztech.tienda.service.PagoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pagos")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8081"})
public class PagoController {

    private static final Logger logger = LoggerFactory.getLogger(PagoController.class);

    @Autowired
    private PagoService pagoService;

    // === NUEVO: CREAR PAGO CON QR EXISTENTE ===
    @PostMapping("/orden/{ordenId}/crear-con-qr")
    public ResponseEntity<?> crearPagoConQR(
            @PathVariable UUID ordenId,
            @RequestParam String qrImageName) {
        
        try {
            logger.info("🎯 Creando pago con QR: {} para orden: {}", qrImageName, ordenId);
            QRResponseDTO response = pagoService.crearPagoConQR(ordenId, qrImageName);
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            logger.error("❌ Error: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Los demás métodos se mantienen igual...
    @PostMapping("/orden/{ordenId}/subir-comprobante")
    public ResponseEntity<?> subirComprobante(
            @PathVariable UUID ordenId,
            @RequestParam("comprobante") MultipartFile comprobante) {
        
        try {
            PagoDTO pagoActualizado = pagoService.subirComprobante(ordenId, comprobante);
            return ResponseEntity.ok(pagoActualizado);
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{pagoId}/verificar")
    public ResponseEntity<?> verificarPago(
            @PathVariable UUID pagoId,
            @RequestBody VerificacionPagoDTO verificacion) {
        
        try {
            PagoDTO pagoVerificado = pagoService.verificarPago(
                pagoId, verificacion.isAprobado(), verificacion.getObservaciones());
            return ResponseEntity.ok(pagoVerificado);
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/orden/{ordenId}")
    public ResponseEntity<?> obtenerPagoPorOrden(@PathVariable UUID ordenId) {
        return pagoService.obtenerPagoPorOrdenId(ordenId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pendientes")
    public ResponseEntity<List<PagoDTO>> obtenerPagosPendientes() {
        return ResponseEntity.ok(pagoService.obtenerPagosPendientes());
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Sistema de pagos Yape operativo ✅");
    }
}