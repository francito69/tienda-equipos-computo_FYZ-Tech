# 🏗️ ARQUITECTURA DEL SISTEMA - FYZ-TECH

## 1. ARQUITECTURA GENERAL

### 1.1 Diagrama de Arquitectura
```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   CLIENTE       │    │    BACKEND       │    │   BASE DE DATOS │
│                 │    │                  │    │                 │
│  Angular 17     │◄──►│  Spring Boot 3   │◄──►│   Supabase      │
│   (Frontend)    │    │   (Backend)      │    │  (PostgreSQL)   │
│                 │    │                  │    │                 │
└─────────────────┘    └──────────────────┘    └─────────────────┘
         │                        │                       │
         │                        │                       │
         ▼                        ▼                       ▼
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Servicios     │    │   Contenedores   │    │   PGAdmin       │
│   Externos      │    │     Docker       │    │   (Incluido)    │
│                 │    │                  │    │                 │
│  - Yape API     │    │  - Desarrollo    │    │  - Management   │
│  - Email SMTP   │    │  - Testing       │    │  - Monitoring   │
└─────────────────┘    └──────────────────┘    └─────────────────┘
```

### 1.2 Stack Tecnológico Confirmado
- **Frontend:** Angular 17 + TypeScript
- **Backend:** Spring Boot 3 + Java 17
- **Base de Datos:** Supabase (PostgreSQL) + PGAdmin
- **Contenedores:** Docker + Docker Compose
- **Autenticación:** JWT + Spring Security

---

## 2. ARQUITECTURA DETALLADA POR CAPAS

### 2.1 Capa de Presentación (Frontend - Angular)

#### Estructura de Módulos Angular
```
src/
├── app/
│   ├── core/                 # Servicios singleton
│   │   ├── auth/
│   │   ├── interceptors/
│   │   └── services/
│   ├── shared/               # Componentes compartidos
│   │   ├── components/
│   │   ├── pipes/
│   │   └── models/
│   ├── features/             # Módulos de funcionalidad
│   │   ├── productos/
│   │   ├── auth/
│   │   ├── carrito/
│   │   ├── checkout/
│   │   └── admin/
│   ├── guards/               # Guards de rutas
│   └── environments/         # Configuraciones
```

#### Componentes Principales
- **ProductoModule:** Catálogo, búsqueda, detalles
- **AuthModule:** Login, registro, recuperación
- **CarritoModule:** Carrito de compras
- **CheckoutModule:** Proceso de pago
- **AdminModule:** Panel administrativo
- **PagoModule:** Módulo de pagos (Yape)

### 2.2 Capa de Negocio (Backend - Spring Boot)

#### Estructura de Paquetes
```
src/main/java/com/fyztech/
├── config/                   # Configuraciones
│   ├── SecurityConfig.java
│   ├── WebConfig.java
│   └── SupabaseConfig.java
├── controller/               # Controladores REST
│   ├── AuthController.java
│   ├── ProductoController.java
│   ├── CarritoController.java
│   ├── OrdenController.java
│   └── PagoController.java
├── service/                  # Lógica de negocio
│   ├── impl/
│   └── interfaces/
├── repository/               # Acceso a datos
│   └── interfaces/
├── model/                    # Entidades y DTOs
│   ├── entity/
│   ├── dto/
│   └── enums/
├── security/                 # Seguridad
│   ├── JwtUtil.java
│   ├── JwtRequestFilter.java
│   └── UserDetailsServiceImpl.java
└── exception/               # Manejo de excepciones
```

### 2.3 Capa de Datos (Supabase - PostgreSQL)

#### Tablas Principales del Sistema
```
📊 TABLAS PRINCIPALES:

1. 👥 usuarios
   - id, email, contraseña, nombres, apellidos, rol, fecha_creacion

2. 📦 categorias
   - id, nombre, descripcion, fecha_creacion

3. 💻 productos  
   - id, nombre, descripcion, precio, stock, categoria_id, imagen_url, especificaciones, activo

4. 🛒 ordenes
   - id, usuario_id, monto_total, estado, direccion_envio, fecha_creacion

5. 📋 items_orden
   - id, orden_id, producto_id, cantidad, precio_unitario

6. 💳 pagos
   - id, orden_id, metodo_pago, monto, estado, qr_code_url, comprobante_url, datos_transaccion
```

*Nota: Los scripts completos de creación de tablas y datos iniciales estarán en `/database/esquema.sql` y `/database/datos-iniciales.sql`*

---

## 3. ARQUITECTURA DE PAGOS ESCALABLE

### 3.1 Patrón Estratégia para Pasarelas de Pago
```java
// Interfaz común para todos los métodos de pago
public interface PasarelaPago {
    RespuestaPago procesarPago(SolicitudPago solicitud);
    EstadoPago verificarEstadoPago(String idPago);
    CodigoQR generarCodigoQR(SolicitudQR solicitud);
}

// Implementación para Yape
@Service
public class PasarelaYape implements PasarelaPago {
    @Override
    public RespuestaPago procesarPago(SolicitudPago solicitud) {
        // Lógica específica para Yape
        // Validar monto, generar referencia, etc.
    }
    
    @Override
    public CodigoQR generarCodigoQR(SolicitudQR solicitud) {
        // Generar QR para Yape con datos del pedido
        // Monto, concepto, número de pedido
    }
    
    @Override
    public EstadoPago verificarEstadoPago(String idPago) {
        // Verificar si el pago fue confirmado
        // Por ahora manual (admin verifica comprobante)
    }
}

// Fábrica para seleccionar pasarela
@Service
public class FabricaPasarelasPago {
    public PasarelaPago obtenerPasarela(String metodoPago) {
        switch (metodoPago.toUpperCase()) {
            case "YAPE":
                return new PasarelaYape();
            case "PLIN":
                return new PasarelaPlin(); // Futura implementación
            case "TARJETA":
                return new PasarelaTarjeta(); // Futura implementación
            case "PAGO_EFECTIVO":
                return new PasarelaPagoEfectivo(); // Futura implementación
            default:
                throw new IllegalArgumentException("Método de pago no soportado: " + metodoPago);
        }
    }
}

// Servicio principal de pagos
@Service
public class ServicioPagos {
    
    @Autowired
    private FabricaPasarelasPago fabricaPasarelas;
    
    public CodigoQR generarQRPago(SolicitudPago solicitud) {
        PasarelaPago pasarela = fabricaPasarelas.obtenerPasarela(solicitud.getMetodoPago());
        return pasarela.generarCodigoQR(convertirSolicitudQR(solicitud));
    }
    
    public void procesarComprobante(String idOrden, MultipartFile comprobante) {
        // Guardar comprobante en almacenamiento
        // Notificar administradores para verificación
        // Actualizar estado de orden a "PENDIENTE_VERIFICACION"
    }
    
    public void verificarPago(String idPago, boolean aprobado, String observaciones) {
        // Lógica para verificación manual por administrador
        // Si aprobado: actualizar orden a "PAGADO"
        // Si rechazado: notificar al cliente y pedir nuevo comprobante
    }
}
```

### 3.2 DTOs para el Sistema de Pagos
```java
// Solicitud de pago
public class SolicitudPago {
    private String ordenId;
    private String metodoPago; // YAPE, PLIN, etc.
    private BigDecimal monto;
    private String moneda; // PEN
    private Map<String, Object> datosAdicionales;
}

// Respuesta de pago
public class RespuestaPago {
    private String idTransaccion;
    private String estado; // PENDIENTE, COMPLETADO, RECHAZADO
    private String mensaje;
    private String urlRedireccion;
    private String qrCodeUrl;
}

// Solicitud para generar QR
public class SolicitudQR {
    private String ordenId;
    private BigDecimal monto;
    private String concepto;
    private String moneda;
}

// Código QR generado
public class CodigoQR {
    private String urlImagen;
    private String contenido;
    private Date fechaExpiracion;
}
```

### 3.3 Flujo de Pago con Yape
```
1. 🎯 Cliente selecciona Yape → Frontend (Angular)
2. 📱 Solicita QR de pago → Backend (Spring Boot)  
3. 🖼️ Genera QR con datos → PasarelaYape.generarCodigoQR()
4. 💰 Cliente paga y sube comprobante → PagoController.procesarComprobante()
5. 👨‍💼 Admin verifica pago → ServicioPagos.verificarPago()
6. ✅ Actualiza estado → Base de datos (Supabase)
```

### 3.4 Endpoints de Pagos
```java
@RestController
@RequestMapping("/api/pagos")
public class PagoController {
    
    @Autowired
    private ServicioPagos servicioPagos;
    
    // Generar QR para Yape
    @PostMapping("/yape/qr")
    public ResponseEntity<CodigoQR> generarQRYape(@RequestBody SolicitudPago solicitud) {
        solicitud.setMetodoPago("YAPE");
        CodigoQR qr = servicioPagos.generarQRPago(solicitud);
        return ResponseEntity.ok(qr);
    }
    
    // Subir comprobante de pago
    @PostMapping("/comprobante")
    public ResponseEntity<?> subirComprobante(
            @RequestParam String ordenId,
            @RequestParam MultipartFile comprobante) {
        servicioPagos.procesarComprobante(ordenId, comprobante);
        return ResponseEntity.ok("Comprobante recibido para verificación");
    }
    
    // Verificar pago (Admin)
    @PutMapping("/admin/verificar")
    public ResponseEntity<?> verificarPago(
            @RequestParam String pagoId,
            @RequestParam boolean aprobado,
            @RequestParam(required = false) String observaciones) {
        servicioPagos.verificarPago(pagoId, aprobado, observaciones);
        return ResponseEntity.ok("Verificación completada");
    }
    
    // Obtener pagos pendientes (Admin)
    @GetMapping("/admin/pendientes")
    public ResponseEntity<List<PagoDTO>> obtenerPagosPendientes() {
        List<PagoDTO> pendientes = servicioPagos.obtenerPagosPendientesVerificacion();
        return ResponseEntity.ok(pendientes);
    }
}
```

---

## 4. CONFIGURACIÓN DOCKER COMPOSE

### 4.1 docker-compose.yml
```yaml
version: '3.8'

services:
  # Frontend - Angular
  frontend:
    build: 
      context: ./frontend
      dockerfile: Dockerfile
    container_name: fytech-frontend
    ports:
      - "4200:4200"
    volumes:
      - ./frontend:/app
      - /app/node_modules
    environment:
      - NODE_ENV=development
    depends_on:
      - backend

  # Backend - Spring Boot
  backend:
    build:
      context: ./backend
      dockerfile: Dockerfile
    container_name: fytech-backend
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/fytech_db
      - SPRING_DATASOURCE_USERNAME=postgres
      - SPRING_DATASOURCE_PASSWORD=password
      - SPRING_JPA_HIBERNATE_DDL_AUTO=update
    depends_on:
      - db
    volumes:
      - ./backend:/app

  # Base de datos - PostgreSQL (Simulación Supabase local)
  db:
    image: postgres:15
    container_name: fytech-db
    environment:
      - POSTGRES_DB=fytech_db
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=password
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
      - ./database/esquema.sql:/docker-entrypoint-initdb.d/01-esquema.sql
      - ./database/datos-iniciales.sql:/docker-entrypoint-initdb.d/02-datos.sql

  # PGAdmin (Para gestión de BD)
  pgadmin:
    image: dpage/pgadmin4
    container_name: fytech-pgadmin
    environment:
      - PGADMIN_DEFAULT_EMAIL=admin@fytech.com
      - PGADMIN_DEFAULT_PASSWORD=admin
    ports:
      - "5050:80"
    depends_on:
      - db

volumes:
  postgres_data:
```

---

## 5. ESTRUCTURA DE ARCHIVOS PARA BASE DE DATOS

### 5.1 `/database/esquema.sql`
```sql
-- Script de creación de tablas 
-- Tablas: usuarios, categorias, productos, ordenes, items_orden, pagos
```

### 5.2 `/database/datos-iniciales.sql`
```sql
-- Datos iniciales para testing
-- Categorías: Laptops, Componentes, Periféricos, etc.
-- Productos de ejemplo
-- Usuario administrador por defecto
```

---

**🏗️ Esta arquitectura proporciona una base sólida y escalable para FYZ-Tech con un sistema de pagos modular que comienza con Yape y puede expandirse fácilmente.**