// backend/src/main/java/com/fyztech/tienda/controller/OrdenController.java
package com.fyztech.tienda.controller;

import com.fyztech.tienda.model.dto.OrdenDTO;
import com.fyztech.tienda.model.dto.OrdenRequestDTO;
import com.fyztech.tienda.service.OrdenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ordenes")
@CrossOrigin(origins = "*")
public class OrdenController {

    private static final Logger logger = LoggerFactory.getLogger(OrdenController.class);

    @Autowired
    private OrdenService ordenService;

    @PostMapping
    public ResponseEntity<OrdenDTO> crearOrden(@RequestBody OrdenRequestDTO ordenRequest) {
        logger.info("🌐 SOLICITUD: Crear orden - Método pago: {}", ordenRequest.getMetodoPago());
        
        OrdenDTO orden = ordenService.crearOrden(ordenRequest);
        logger.info("📤 RESPUESTA: Orden creada - ID: {}, Total: {}", 
                   orden.getId(), orden.getMontoTotal());
        
        return ResponseEntity.ok(orden);
    }

    @GetMapping
    public ResponseEntity<List<OrdenDTO>> obtenerOrdenesUsuario() {
        logger.info("🌐 SOLICITUD: Obtener órdenes del usuario");
        
        List<OrdenDTO> ordenes = ordenService.obtenerOrdenesUsuario();
        logger.info("📤 RESPUESTA: Enviando {} órdenes", ordenes.size());
        
        return ResponseEntity.ok(ordenes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenDTO> obtenerOrdenPorId(@PathVariable UUID id) {
        logger.info("🌐 SOLICITUD: Obtener orden por ID: {}", id);
        
        return ordenService.obtenerOrdenPorId(id)
                .map(orden -> {
                    logger.info("📤 RESPUESTA: Orden encontrada - ID: {}", orden.getId());
                    return ResponseEntity.ok(orden);
                })
                .orElseGet(() -> {
                    logger.warn("📤 RESPUESTA: Orden no encontrada - ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }
}