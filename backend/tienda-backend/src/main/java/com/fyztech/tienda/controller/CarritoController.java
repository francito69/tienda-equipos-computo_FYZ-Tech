// backend/src/main/java/com/fyztech/tienda/controller/CarritoController.java
package com.fyztech.tienda.controller;

import com.fyztech.tienda.model.dto.CarritoResponseDTO;
import com.fyztech.tienda.service.OrdenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/carrito")
@CrossOrigin(origins = "*")
public class CarritoController {

    private static final Logger logger = LoggerFactory.getLogger(CarritoController.class);

    @Autowired
    private OrdenService ordenService;

    @GetMapping
    public ResponseEntity<CarritoResponseDTO> obtenerCarrito() {
        logger.info("🌐 SOLICITUD: Obtener carrito");
        
        CarritoResponseDTO carrito = ordenService.obtenerCarrito();
        logger.info("📤 RESPUESTA: Carrito con {} items, total: {}", 
                   carrito.getTotalItems(), carrito.getTotal());
        
        return ResponseEntity.ok(carrito);
    }

    @PostMapping("/agregar")
    public ResponseEntity<CarritoResponseDTO> agregarAlCarrito(
            @RequestParam UUID productoId,
            @RequestParam Integer cantidad) {
        logger.info("🌐 SOLICITUD: Agregar al carrito - Producto: {}, Cantidad: {}", 
                   productoId, cantidad);
        
        CarritoResponseDTO carrito = ordenService.agregarAlCarrito(productoId, cantidad);
        logger.info("📤 RESPUESTA: Carrito actualizado - Total items: {}", carrito.getTotalItems());
        
        return ResponseEntity.ok(carrito);
    }

    @PutMapping("/actualizar")
    public ResponseEntity<CarritoResponseDTO> actualizarCantidad(
            @RequestParam UUID productoId,
            @RequestParam Integer cantidad) {
        logger.info("🌐 SOLICITUD: Actualizar cantidad - Producto: {}, Cantidad: {}", 
                   productoId, cantidad);
        
        CarritoResponseDTO carrito = ordenService.actualizarCantidadCarrito(productoId, cantidad);
        logger.info("📤 RESPUESTA: Cantidad actualizada");
        
        return ResponseEntity.ok(carrito);
    }

    @DeleteMapping("/remover")
    public ResponseEntity<CarritoResponseDTO> removerDelCarrito(@RequestParam UUID productoId) {
        logger.info("🌐 SOLICITUD: Remover del carrito - Producto: {}", productoId);
        
        CarritoResponseDTO carrito = ordenService.removerDelCarrito(productoId);
        logger.info("📤 RESPUESTA: Producto removido del carrito");
        
        return ResponseEntity.ok(carrito);
    }
}