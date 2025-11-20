// backend/src/main/java/com/fyztech/tienda/service/ProductoService.java
package com.fyztech.tienda.service;

import com.fyztech.tienda.model.dto.ProductoDTO;
import com.fyztech.tienda.model.entity.Producto;
import com.fyztech.tienda.repository.ProductoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    private static final Logger logger = LoggerFactory.getLogger(ProductoService.class);

    @Autowired
    private ProductoRepository productoRepository;

    public List<ProductoDTO> obtenerTodosProductosActivos() {
        logger.info("📦 Obteniendo todos los productos activos");
        
        List<Producto> productos = productoRepository.findByActivoTrue();
        logger.info("✅ Encontrados {} productos activos", productos.size());
        
        return productos.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Optional<ProductoDTO> obtenerProductoPorId(UUID id) {
        logger.info("🔍 Buscando producto por ID: {}", id);
        
        Optional<Producto> producto = productoRepository.findByIdAndActivoTrue(id);
        
        if (producto.isPresent()) {
            logger.info("✅ Producto encontrado: {}", producto.get().getNombre());
            return Optional.of(convertirADTO(producto.get()));
        } else {
            logger.warn("⚠️ Producto no encontrado o inactivo: {}", id);
            return Optional.empty();
        }
    }

    public List<ProductoDTO> buscarProductos(String busqueda) {
        logger.info("🔎 Buscando productos con término: '{}'", busqueda);
        
        List<Producto> productos = productoRepository.buscarProductos(busqueda);
        logger.info("✅ Encontrados {} productos para búsqueda: '{}'", productos.size(), busqueda);
        
        return productos.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<ProductoDTO> obtenerProductosPorCategoria(UUID categoriaId) {
        logger.info("🏷️ Obteniendo productos por categoría ID: {}", categoriaId);
        
        List<Producto> productos = productoRepository.findByCategoriaIdAndActivoTrue(categoriaId);
        logger.info("✅ Encontrados {} productos para categoría: {}", productos.size(), categoriaId);
        
        return productos.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<ProductoDTO> obtenerProductosConStock() {
        logger.info("📊 Obteniendo productos con stock disponible");
        
        List<Producto> productos = productoRepository.findByStockGreaterThanAndActivoTrue(0);
        logger.info("✅ Encontrados {} productos con stock", productos.size());
        
        return productos.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private ProductoDTO convertirADTO(Producto producto) {
        return new ProductoDTO(
            producto.getId(),
            producto.getNombre(),
            producto.getDescripcion(),
            producto.getPrecio(),
            producto.getStock(),
            producto.getImagenUrl(),
            producto.getEspecificaciones(),
            producto.getActivo(),
            producto.getFechaCreacion(),
            producto.getCategoria() != null ? producto.getCategoria().getNombre() : "Sin categoría",
            producto.getCategoria() != null ? producto.getCategoria().getId() : null
        );
    }
}