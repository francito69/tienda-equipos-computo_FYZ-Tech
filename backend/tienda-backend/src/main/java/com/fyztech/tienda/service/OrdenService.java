// backend/src/main/java/com/fyztech/tienda/service/OrdenService.java
package com.fyztech.tienda.service;

import com.fyztech.tienda.model.dto.*;
import com.fyztech.tienda.model.entity.*;
import com.fyztech.tienda.repository.OrdenRepository;
import com.fyztech.tienda.repository.ProductoRepository;
import com.fyztech.tienda.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;  // ← AGREGAR ESTE IMPORT

@Service
@Transactional
public class OrdenService {

    private static final Logger logger = LoggerFactory.getLogger(OrdenService.class);

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Carrito en memoria (en producción usar Redis o base de datos)
    private final Map<String, List<CarritoItemDTO>> carritos = new ConcurrentHashMap<>();

    // === CARRITO ===

    public CarritoResponseDTO obtenerCarrito() {
        String usuarioEmail = obtenerUsuarioActual();
        logger.info("🛒 Obteniendo carrito para usuario: {}", usuarioEmail);

        List<CarritoItemDTO> items = carritos.getOrDefault(usuarioEmail, new ArrayList<>());
        BigDecimal total = calcularTotalCarrito(items);
        Integer totalItems = items.stream().mapToInt(CarritoItemDTO::getCantidad).sum();

        logger.info("✅ Carrito obtenido - {} items, total: {}", totalItems, total);
        return new CarritoResponseDTO(items, total, totalItems);
    }

    public CarritoResponseDTO agregarAlCarrito(UUID productoId, Integer cantidad) {
        String usuarioEmail = obtenerUsuarioActual();
        logger.info("➕ Agregando al carrito - Producto: {}, Cantidad: {}, Usuario: {}", 
                   productoId, cantidad, usuarioEmail);

        Producto producto = productoRepository.findByIdAndActivoTrue(productoId)
                .orElseThrow(() -> {
                    logger.error("❌ Producto no encontrado: {}", productoId);
                    return new RuntimeException("Producto no encontrado");
                });

        if (producto.getStock() < cantidad) {
            logger.error("❌ Stock insuficiente - Producto: {}, Stock: {}, Solicitado: {}", 
                        producto.getNombre(), producto.getStock(), cantidad);
            throw new RuntimeException("Stock insuficiente");
        }

        List<CarritoItemDTO> carrito = carritos.getOrDefault(usuarioEmail, new ArrayList<>());

        // Verificar si el producto ya está en el carrito
        Optional<CarritoItemDTO> itemExistente = carrito.stream()
                .filter(item -> item.getProductoId().equals(productoId))
                .findFirst();

        if (itemExistente.isPresent()) {
            // Actualizar cantidad
            itemExistente.get().setCantidad(itemExistente.get().getCantidad() + cantidad);
            logger.info("📝 Producto actualizado en carrito - Nuevas unidades: {}", 
                       itemExistente.get().getCantidad());
        } else {
            // Agregar nuevo item
            CarritoItemDTO nuevoItem = new CarritoItemDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getPrecio(),
                cantidad,
                producto.getImagenUrl()
            );
            carrito.add(nuevoItem);
            logger.info("🆕 Producto agregado al carrito - {}", producto.getNombre());
        }

        carritos.put(usuarioEmail, carrito);

        CarritoResponseDTO response = obtenerCarrito();
        logger.info("✅ Carrito actualizado - Total items: {}, Total: {}", 
                   response.getTotalItems(), response.getTotal());
        
        return response;
    }

    public CarritoResponseDTO actualizarCantidadCarrito(UUID productoId, Integer cantidad) {
        String usuarioEmail = obtenerUsuarioActual();
        logger.info("✏️ Actualizando cantidad - Producto: {}, Nueva cantidad: {}", 
                   productoId, cantidad);

        if (cantidad <= 0) {
            return removerDelCarrito(productoId);
        }

        List<CarritoItemDTO> carrito = carritos.get(usuarioEmail);
        if (carrito == null) {
            logger.error("❌ Carrito no encontrado para usuario: {}", usuarioEmail);
            throw new RuntimeException("Carrito no encontrado");
        }

        carrito.stream()
                .filter(item -> item.getProductoId().equals(productoId))
                .findFirst()
                .ifPresentOrElse(
                    item -> {
                        // Verificar stock
                        Producto producto = productoRepository.findByIdAndActivoTrue(productoId)
                                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
                        
                        if (producto.getStock() < cantidad) {
                            throw new RuntimeException("Stock insuficiente");
                        }
                        
                        item.setCantidad(cantidad);
                        logger.info("✅ Cantidad actualizada - Producto: {}, Cantidad: {}", 
                                   producto.getNombre(), cantidad);
                    },
                    () -> {
                        logger.error("❌ Producto no encontrado en carrito: {}", productoId);
                        throw new RuntimeException("Producto no encontrado en carrito");
                    }
                );

        return obtenerCarrito();
    }

    public CarritoResponseDTO removerDelCarrito(UUID productoId) {
        String usuarioEmail = obtenerUsuarioActual();
        logger.info("🗑️ Removiendo del carrito - Producto: {}", productoId);

        List<CarritoItemDTO> carrito = carritos.get(usuarioEmail);
        if (carrito != null) {
            boolean removido = carrito.removeIf(item -> item.getProductoId().equals(productoId));
            if (removido) {
                logger.info("✅ Producto removido del carrito");
            } else {
                logger.warn("⚠️ Producto no encontrado en carrito: {}", productoId);
            }
        }

        return obtenerCarrito();
    }

    public void limpiarCarrito() {
        String usuarioEmail = obtenerUsuarioActual();
        logger.info("🧹 Limpiando carrito para usuario: {}", usuarioEmail);
        
        carritos.remove(usuarioEmail);
        logger.info("✅ Carrito limpiado");
    }

    private BigDecimal calcularTotalCarrito(List<CarritoItemDTO> items) {
        return items.stream()
                .map(CarritoItemDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // === ÓRDENES ===

    public OrdenDTO crearOrden(OrdenRequestDTO ordenRequest) {
        String usuarioEmail = obtenerUsuarioActual();
        logger.info("🛍️ Creando orden para usuario: {}", usuarioEmail);

        Usuario usuario = usuarioRepository.findByEmail(usuarioEmail)
                .orElseThrow(() -> {
                    logger.error("❌ Usuario no encontrado: {}", usuarioEmail);
                    return new RuntimeException("Usuario no encontrado");
                });

        List<CarritoItemDTO> carrito = carritos.get(usuarioEmail);
        if (carrito == null || carrito.isEmpty()) {
            logger.error("❌ Carrito vacío para usuario: {}", usuarioEmail);
            throw new RuntimeException("El carrito está vacío");
        }

        // Crear la orden
        Orden orden = new Orden(usuario, ordenRequest.getDireccionEnvio());

        // Agregar items a la orden y actualizar stock
        for (CarritoItemDTO item : carrito) {
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + item.getProductoId()));

            // Verificar stock nuevamente
            if (producto.getStock() < item.getCantidad()) {
                logger.error("❌ Stock insuficiente al crear orden - Producto: {}, Stock: {}, Solicitado: {}", 
                           producto.getNombre(), producto.getStock(), item.getCantidad());
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombre());
            }

            // Actualizar stock
            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);

            // Crear item de orden
            OrdenItem ordenItem = new OrdenItem(producto, item.getCantidad());
            orden.agregarItem(ordenItem);

            logger.info("📦 Item agregado a orden - Producto: {}, Cantidad: {}", 
                       producto.getNombre(), item.getCantidad());
        }

        // Guardar orden
        Orden ordenGuardada = ordenRepository.save(orden);
        logger.info("✅ Orden creada exitosamente - ID: {}, Total: {}", 
                   ordenGuardada.getId(), ordenGuardada.getMontoTotal());

        // Limpiar carrito
        limpiarCarrito();

        return convertirOrdenADTO(ordenGuardada);
    }

    public List<OrdenDTO> obtenerOrdenesUsuario() {
        String usuarioEmail = obtenerUsuarioActual();
        logger.info("📋 Obteniendo órdenes para usuario: {}", usuarioEmail);

        Usuario usuario = usuarioRepository.findByEmail(usuarioEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Orden> ordenes = ordenRepository.findByUsuarioIdOrderByFechaCreacionDesc(usuario.getId());
        logger.info("✅ Encontradas {} órdenes para usuario: {}", ordenes.size(), usuarioEmail);

        return ordenes.stream()
                .map(this::convertirOrdenADTO)
                .collect(Collectors.toList());
    }

    public Optional<OrdenDTO> obtenerOrdenPorId(UUID ordenId) {
        logger.info("🔍 Buscando orden por ID: {}", ordenId);

        return ordenRepository.findById(ordenId)
                .map(orden -> {
                    logger.info("✅ Orden encontrada - ID: {}, Estado: {}", orden.getId(), orden.getEstado());
                    return convertirOrdenADTO(orden);
                });
    }

    private OrdenDTO convertirOrdenADTO(Orden orden) {
        List<OrdenItemDTO> itemsDTO = orden.getItems().stream()
                .map(item -> new OrdenItemDTO(
                    item.getProducto().getId(),
                    item.getProducto().getNombre(),
                    item.getCantidad(),
                    item.getPrecioUnitario(),
                    item.getSubtotal()
                ))
                .collect(Collectors.toList());

        return new OrdenDTO(
            orden.getId(),
            orden.getMontoTotal(),
            orden.getEstado(),
            orden.getDireccionEnvio(),
            orden.getFechaCreacion(),
            itemsDTO
        );
    }

    private String obtenerUsuarioActual() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}