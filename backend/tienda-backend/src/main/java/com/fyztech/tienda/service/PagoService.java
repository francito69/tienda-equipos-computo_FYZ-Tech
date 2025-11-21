// backend/src/main/java/com/fyztech/tienda/service/PagoService.java
package com.fyztech.tienda.service;

import com.fyztech.tienda.model.dto.PagoDTO;
import com.fyztech.tienda.model.dto.QRResponseDTO;
import com.fyztech.tienda.model.entity.Orden;
import com.fyztech.tienda.model.entity.Pago;
import com.fyztech.tienda.repository.OrdenRepository;
import com.fyztech.tienda.repository.PagoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class PagoService {

    private static final Logger logger = LoggerFactory.getLogger(PagoService.class);

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private OrdenRepository ordenRepository;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Value("${app.yape.phone:987126753}")
    private String yapePhoneNumber;

    // === CREAR PAGO Y ASIGNAR QR ===

    public QRResponseDTO crearPagoConQR(UUID ordenId, String qrImageName) {
        logger.info("💰 Creando pago con QR para orden: {}", ordenId);

        Orden orden = ordenRepository.findById(ordenId)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        Optional<Pago> pagoExistente = pagoRepository.findByOrdenId(ordenId);
        if (pagoExistente.isPresent()) {
            throw new RuntimeException("Ya existe un pago para esta orden");
        }

        // Crear pago
        Pago pago = new Pago(orden, "YAPE");
        pago.setQrImageName(qrImageName); // Esto se guardará en qr_code_url
        
        Pago pagoGuardado = pagoRepository.save(pago);
        logger.info("✅ Pago creado - ID: {}", pagoGuardado.getId());

        // Preparar respuesta
        QRResponseDTO response = new QRResponseDTO();
        response.setQrImageUrl("/uploads/qr/" + qrImageName);
        response.setMonto(orden.getMontoTotal());
        response.setNumeroCelular(yapePhoneNumber);
        response.setConcepto("Orden #" + orden.getId().toString().substring(0, 8));
        response.setInstrucciones(
            "1. Abre Yape y ve a 'Pagar'\n" +
            "2. Escanea este código QR\n" + 
            "3. Confirma el pago de S/ " + orden.getMontoTotal() + "\n" +
            "4. Toma captura y sube el comprobante"
        );

        return response;
    }

    // === SUBIR COMPROBANTE ===
    public PagoDTO subirComprobante(UUID ordenId, MultipartFile comprobante) {
        logger.info("📤 Subiendo comprobante para orden: {}", ordenId);

        // 1. Buscar pago
        Pago pago = pagoRepository.findByOrdenId(ordenId)
                .orElseThrow(() -> {
                    logger.error("❌ Pago no encontrado para orden: {}", ordenId);
                    return new RuntimeException("Pago no encontrado");
                });

        // 2. Validar archivo
        if (comprobante.isEmpty()) {
            throw new RuntimeException("El archivo está vacío");
        }

        if (!esImagenValida(comprobante)) {
            throw new RuntimeException("Solo se permiten imágenes JPG, PNG o PDF");
        }

        try {
            // 3. Crear directorios
            Path comprobantesDir = Paths.get(uploadDir, "comprobantes");
            if (!Files.exists(comprobantesDir)) {
                Files.createDirectories(comprobantesDir);
            }

            // 4. Guardar archivo
            String extension = obtenerExtension(comprobante.getOriginalFilename());
            String nombreArchivo = "comprobante_" + ordenId + "_" + System.currentTimeMillis() + "." + extension;
            Path filePath = comprobantesDir.resolve(nombreArchivo);
            
            Files.copy(comprobante.getInputStream(), filePath);

            // 5. Actualizar pago
            pago.setComprobanteImageName(nombreArchivo);
            pago.setEstado("PENDIENTE_VERIFICACION");
            pago.setFechaActualizacion(LocalDateTime.now());

            Pago pagoActualizado = pagoRepository.save(pago);
            logger.info("✅ Comprobante subido: {}", nombreArchivo);

            return convertirPagoADTO(pagoActualizado);

        } catch (IOException e) {
            logger.error("❌ Error al subir comprobante: {}", e.getMessage());
            throw new RuntimeException("Error al subir comprobante: " + e.getMessage());
        }
    }

    // === VERIFICAR PAGO (ADMIN) ===
    public PagoDTO verificarPago(UUID pagoId, boolean aprobado, String observaciones) {
        logger.info("🔍 Verificando pago: {}, Aprobado: {}", pagoId, aprobado);

        Pago pago = pagoRepository.findById(pagoId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        Orden orden = pago.getOrden();

        if (aprobado) {
            pago.setEstado("VERIFICADO");
            orden.setEstado("CONFIRMADA");
            logger.info("✅ Pago VERIFICADO - Orden: {}", orden.getId());
        } else {
            pago.setEstado("RECHAZADO");
            orden.setEstado("PAGO_RECHAZADO");
            logger.info("❌ Pago RECHAZADO: {}", observaciones);
        }

        pago.setFechaActualizacion(LocalDateTime.now());
        
        Pago pagoActualizado = pagoRepository.save(pago);
        ordenRepository.save(orden);

        return convertirPagoADTO(pagoActualizado);
    }

    // === CONSULTAS ===
    public Optional<PagoDTO> obtenerPagoPorOrdenId(UUID ordenId) {
        return pagoRepository.findByOrdenId(ordenId)
                .map(this::convertirPagoADTO);
    }

    public List<PagoDTO> obtenerPagosPendientes() {
        return pagoRepository.findByEstado("PENDIENTE_VERIFICACION")
                .stream()
                .map(this::convertirPagoADTO)
                .collect(Collectors.toList());
    }

    public List<PagoDTO> obtenerPagosPorEstado(String estado) {
        return pagoRepository.findByEstado(estado)
                .stream()
                .map(this::convertirPagoADTO)
                .collect(Collectors.toList());
    }

    // === MÉTODOS PRIVADOS ===
    private boolean esImagenValida(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && 
               (contentType.startsWith("image/") || contentType.equals("application/pdf"));
    }

    private String obtenerExtension(String filename) {
        if (filename == null) return "jpg";
        int lastDot = filename.lastIndexOf(".");
        return lastDot == -1 ? "jpg" : filename.substring(lastDot + 1);
    }

    private PagoDTO convertirPagoADTO(Pago pago) {
        // USAR LOS CAMPOS EXISTENTES DE TU BD
        String qrUrl = pago.getQrImageName() != null ? 
            "/uploads/qr/" + pago.getQrImageName() : null;
            
        String comprobanteUrl = pago.getComprobanteImageName() != null ? 
            "/uploads/comprobantes/" + pago.getComprobanteImageName() : null;

        return new PagoDTO(
            pago.getId(),
            pago.getMetodoPago(),
            pago.getMonto(),
            pago.getEstado(),
            qrUrl,
            comprobanteUrl,
            pago.getFechaCreacion(),
            pago.getOrden().getId(),
            pago.getOrden().getEstado()
        );
    }
}