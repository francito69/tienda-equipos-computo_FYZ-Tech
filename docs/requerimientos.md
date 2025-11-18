# 📋 REQUERIMIENTOS - FYZ-TECH

## 1. REQUERIMIENTOS FUNCIONALES

### 1.1 Gestión de Usuarios 👥
**RF-001: Registro de Usuario**
- Como usuario anónimo, quiero registrarme con email y contraseña para crear una cuenta
- **Campos requeridos:** nombre, apellido, email, contraseña, confirmar contraseña
- **Validaciones:** email único, contraseña mínimo 6 caracteres

**RF-002: Autenticación de Usuario**
- Como usuario registrado, quiero iniciar sesión con email y contraseña
- Como usuario autenticado, quiero cerrar sesión
- **Seguridad:** tokens JWT, contraseñas encriptadas

**RF-003: Roles de Usuario**
- **Cliente:** puede comprar productos, ver historial
- **Administrador:** puede gestionar productos, ver órdenes, gestionar usuarios

### 1.2 Catálogo de Productos 📦
**RF-004: Gestión de Productos (Admin)**
- Como administrador, quiero agregar nuevos productos al catálogo
- **Campos producto:** nombre, descripción, precio, categoría, stock, imagen, especificaciones técnicas
- Como administrador, quiero editar y eliminar productos

**RF-005: Visualización de Productos**
- Como usuario, quiero ver la lista de productos disponibles
- Como usuario, quiero buscar productos por nombre
- Como usuario, quiero filtrar productos por categoría
- Como usuario, quiero ver los detalles de un producto específico

### 1.3 Carrito de Compras 🛒
**RF-006: Gestión del Carrito**
- Como usuario, quiero agregar productos al carrito de compras
- Como usuario, quiero modificar las cantidades en el carrito
- Como usuario, quiero eliminar productos del carrito
- Como usuario, quiero ver el total de mi compra

**RF-007: Persistencia del Carrito**
- Como usuario autenticado, quiero que mi carrito se guarde entre sesiones
- Como usuario no autenticado, quiero tener un carrito temporal

### 1.4 Proceso de Compra y Pagos 💳
**RF-008: Checkout Básico**
- Como usuario, quiero proceder al checkout con los productos de mi carrito
- **Datos requeridos:** dirección de envío, información de contacto
- Como usuario, quiero confirmar mi pedido

**RF-009: Sistema de Pagos Escalable**
- **Arquitectura escalable** para agregar múltiples métodos de pago
- **Métodos planeados:** Yape, Plin, Pago Efectivo, Tarjetas (Visa/Mastercard), PayPal
- **Inicialmente implementado:** Yape

**RF-010: Pago con Yape**
- Como usuario, quiero pagar mi pedido usando Yape
- Como usuario, quiero escanear un código QR para realizar el pago
- Como usuario, quiero subir mi comprobante de pago después de realizar la transacción

**RF-011: Verificación de Pagos Yape (Admin)**
- Como administrador, quiero ver los pedidos pendientes de verificación de pago
- Como administrador, quiero confirmar la recepción de pagos Yape
- Como administrador, quiero rechazar comprobantes inválidos

**RF-012: Gestión de Órdenes**
- Como usuario, quiero ver el estado de mis pedidos
- Como administrador, quiero ver todas las órdenes
- Como administrador, quiero actualizar el estado de las órdenes (pendiente, enviado, entregado)

## 2. REQUERIMIENTOS NO FUNCIONALES

### 2.1 Seguridad 🔒
**RNF-001: Autenticación Segura**
- Las contraseñas deben estar encriptadas (bcrypt)
- Uso de JWT para sesiones
- Validación de entrada en todos los formularios

**RNF-002: Protección de Datos**
- HTTPS obligatorio en producción
- No almacenar información sensible de tarjetas
- Validación contra SQL injection y XSS

**RNF-003: Seguridad en Pagos**
- Validación de comprobantes subidos
- Escaneo de archivos maliciosos
- Registro de auditoría para transacciones

### 2.2 Rendimiento ⚡
**RNF-004: Tiempos de Respuesta**
- Carga inicial de la página: < 3 segundos
- Búsqueda de productos: < 1 segundo
- Proceso de checkout: < 2 segundos

**RNF-005: Disponibilidad**
- Sistema disponible 99% del tiempo
- Tolerancia a fallos en base de datos

### 2.3 Escalabilidad 📈
**RNF-006: Arquitectura Escalable**
- Diseño modular para agregar nuevos métodos de pago
- Base de datos preparada para múltiples gateways
- API extensible para integraciones futuras

**RNF-007: Crecimiento de Usuarios**
- Soporte para 1000 usuarios concurrentes
- Base de datos optimizada para grandes volúmenes

### 2.4 Usabilidad 📱
**RNF-008: Diseño Responsive**
- Compatible con desktop, tablet y móvil
- Navegación intuitiva
- Proceso de compra en máximo 5 pasos

**RNF-009: Experiencia de Pago**
- Interfaz clara para pago con Yape
- Proceso intuitivo para subir comprobantes
- Feedback inmediato de acciones

### 2.5 Compatibilidad 🖥️
**RNF-010: Navegadores Soportados**
- Chrome 90+
- Firefox 88+
- Safari 14+
- Edge 90+

## 3. USER STORIES PRIORIZADAS

### 🚀 SPRINT 1 (Críticas - Semana 1-2)
**US-001:** Como usuario, quiero registrarme en el sitio web  
**US-002:** Como usuario, quiero iniciar sesión en mi cuenta  
**US-003:** Como administrador, quiero agregar nuevos productos  
**US-004:** Como usuario, quiero ver la lista de productos  

### 🚀 SPRINT 2 (Críticas - Semana 3-4)
**US-005:** Como usuario, quiero buscar productos por nombre  
**US-006:** Como usuario, quiero agregar productos al carrito  
**US-007:** Como usuario, quiero ver el total de mi carrito  
**US-008:** Como usuario, quiero proceder al checkout  

### 🚀 SPRINT 3 (Críticas - Semana 5-6)
**US-009:** Como usuario, quiero confirmar mi pedido  
**US-010:** Como usuario, quiero pagar mi pedido con Yape escaneando un QR  
**US-011:** Como usuario, quiero subir mi comprobante de pago Yape  
**US-012:** Como administrador, quiero ver las órdenes de compra  
**US-013:** Como administrador, quiero verificar los comprobantes de pago Yape  
**US-014:** Como usuario, quiero ver mi historial de pedidos  
**US-015:** Como administrador, quiero actualizar el estado de las órdenes  

### 📈 SPRINT 4 (Escalabilidad - Futuro)
**US-016:** Como usuario, quiero pagar con Plin  
**US-017:** Como usuario, quiero pagar con tarjeta de crédito  
**US-018:** Como usuario, quiero pagar en efectivo (Pago Efectivo)  
**US-019:** Como usuario, quiero pagar con PayPal  

## 4. CRITERIOS DE ACEPTACIÓN

### Para US-001 (Registro de Usuario)
- [ ] El formulario valida email único
- [ ] Las contraseñas coinciden
- [ ] Muestra mensaje de éxito al registrar
- [ ] Redirige al login después del registro
- [ ] Envía email de confirmación (opcional)

### Para US-003 (Agregar Productos - Admin)
- [ ] Formulario con validación de campos requeridos
- [ ] Precio debe ser numérico positivo
- [ ] Stock debe ser entero no negativo
- [ ] Muestra preview de imagen seleccionada
- [ ] Confirma creación exitosa del producto

### Para US-006 (Agregar al Carrito)
- [ ] Muestra botón "Agregar al carrito" en cada producto
- [ ] Actualiza contador del carrito en header
- [ ] Muestra mensaje de confirmación
- [ ] No permite agregar si stock es 0
- [ ] Persiste el carrito para usuarios logueados

### Para US-010 (Pago con Yape)
- [ ] Muestra opción "Pagar con Yape" en el checkout
- [ ] Genera y muestra código QR con monto exacto
- [ ] Incluye número de celular de la empresa para transferencia manual
- [ ] Muestra instrucciones claras para el pago
- [ ] El QR contiene: monto, concepto (número de pedido), información de la empresa

### Para US-011 (Subir Comprobante)
- [ ] Después de seleccionar Yape, muestra formulario para subir comprobante
- [ ] Acepta formatos: JPG, PNG, PDF
- [ ] Límite de tamaño: 5MB
- [ ] Muestra preview del comprobante subido
- [ ] Envía confirmación de recepción del comprobante

### Para US-013 (Verificación de Pagos - Admin)
- [ ] Panel admin muestra lista de pedidos con "Pago pendiente de verificación"
- [ ] Permite visualizar comprobantes subidos
- [ ] Botones "Confirmar Pago" y "Rechazar Pago"
- [ ] En caso de rechazo, permite enviar motivo al cliente
- [ ] Al confirmar, cambia estado del pedido a "Pago confirmado"

## 5. ARQUITECTURA DE PAGOS ESCALABLE

### 5.1 Diseño del Sistema de Pagos
```
Sistema de Pagos FYZ-Tech
│
├── PaymentGateway (Interfaz)
│   ├── YapeGateway ✅ (Implementado)
│   ├── PlinGateway 🔄 (Futuro)
│   ├── CardGateway 🔄 (Futuro)
│   ├── PagoEfectivoGateway 🔄 (Futuro)
│   └── PayPalGateway 🔄 (Futuro)
│
├── PaymentProcessor
│   ├── Valida métodos disponibles
│   ├── Gera QR codes (Yape/Plin)
│   └── Procesa confirmaciones
│
└── PaymentValidator
    ├── Verifica comprobantes
    ├── Valida montos
    └── Auditoría de transacciones
```

### 5.2 Flujo de Pago con Yape
1. **Selección de método** → Usuario elige "Pagar con Yape"
2. **Generación de QR** → Sistema crea QR con datos del pedido
3. **Pago usuario** → Usuario escanea y paga con Yape
4. **Subir comprobante** → Usuario sube screenshot del pago
5. **Verificación admin** → Admin valida comprobante
6. **Confirmación** → Sistema actualiza estado del pedido

### 5.3 Preparación para Métodos Futuros
- **Base de datos:** Campos para múltiples gateways
- **API:** Endpoints genéricos para procesamiento de pagos
- **Frontend:** Componente modular para diferentes métodos
- **Backend:** Patrón Strategy para gateways de pago

## 6. DEFINICIÓN DE TERMINADO

### Para cada User Story:
- [ ] Código desarrollado y probado
- [ ] Pruebas unitarias pasando
- [ ] Integración con el sistema completa
- [ ] Documentación actualizada
- [ ] Revisión de código aprobada
- [ ] Desplegado en ambiente de desarrollo

### Para el MVP Completo:
- [ ] Todas las user stories de Sprint 1-3 completadas
- [ ] Sistema de pagos con Yape funcionando
- [ ] Arquitectura escalable para métodos futuros
- [ ] Pruebas de integración exitosas
- [ ] Documentación técnica completa
- [ ] Despliegue en ambiente de producción
- [ ] Manual de usuario básico

---

## 📝 NOTAS TÉCNICAS

### Métodos de Pago Planificados:
1. **Yape** ✅ (Prioritario - MVP)
2. **Plin** 🔄 (Siguiente fase)
3. **Pago Efectivo** 🔄 (Agentes/ Bancos)
4. **Tarjetas** 🔄 (Visa/Mastercard via Culqi)
5. **PayPal** 🔄 (Internacional)

### Consideraciones de Seguridad:
- No almacenar datos sensibles de pago
- Validar extensiones y tipos de archivo en comprobantes
- Logs de auditoría para todas las transacciones
- Límites de tamaño para archivos subidos

---

**📋 Este documento establece los requerimientos completos para el desarrollo de FYZ-Tech, con un enfoque en pagos escalables comenzando con Yape como método principal.**

**Fecha de creación:** 17/11/2025  
**Última actualización:** 17/11/2025 HRS 20:30 
**Versión:** 1.0