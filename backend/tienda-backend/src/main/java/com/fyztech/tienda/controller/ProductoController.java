// backend/src/main/java/com/fyztech/tienda/controller/ProductoController.java
package com.fyztech.tienda.controller;

import com.fyztech.tienda.model.dto.ProductoDTO;
import com.fyztech.tienda.service.ProductoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    private static final Logger logger = LoggerFactory.getLogger(ProductoController.class);

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> obtenerTodosProductos() {
        logger.info("🌐 SOLICITUD: Obtener todos los productos");
        
        List<ProductoDTO> productos = productoService.obtenerTodosProductosActivos();
        logger.info("📤 RESPUESTA: Enviando {} productos", productos.size());
        
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerProductoPorId(@PathVariable UUID id) {
        logger.info("🌐 SOLICITUD: Obtener producto por ID: {}", id);
        
        return productoService.obtenerProductoPorId(id)
                .map(producto -> {
                    logger.info("📤 RESPUESTA: Producto encontrado - {}", producto.getNombre());
                    return ResponseEntity.ok(producto);
                })
                .orElseGet(() -> {
                    logger.warn("📤 RESPUESTA: Producto no encontrado - ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ProductoDTO>> buscarProductos(@RequestParam String q) {
        logger.info("🌐 SOLICITUD: Buscar productos - Término: '{}'", q);
        
        List<ProductoDTO> productos = productoService.buscarProductos(q);
        logger.info("📤 RESPUESTA: Enviando {} productos para búsqueda '{}'", productos.size(), q);
        
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ProductoDTO>> obtenerProductosPorCategoria(@PathVariable UUID categoriaId) {
        logger.info("🌐 SOLICITUD: Productos por categoría - ID: {}", categoriaId);
        
        List<ProductoDTO> productos = productoService.obtenerProductosPorCategoria(categoriaId);
        logger.info("📤 RESPUESTA: Enviando {} productos para categoría {}", productos.size(), categoriaId);
        
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/con-stock")
    public ResponseEntity<List<ProductoDTO>> obtenerProductosConStock() {
        logger.info("🌐 SOLICITUD: Productos con stock disponible");
        
        List<ProductoDTO> productos = productoService.obtenerProductosConStock();
        logger.info("📤 RESPUESTA: Enviando {} productos con stock", productos.size());
        
        return ResponseEntity.ok(productos);
    }
}