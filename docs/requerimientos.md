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

# 📋 CASOS DE USO - FYZ-TECH

## 🎯 DIAGRAMA DE CASOS DE USO PRINCIPAL

```
┌─────────────────┐
│   Sistema       │
│   FYZ-TECH      │
└─────────────────┘
         │
         ├─── Gestión de Usuarios
         ├─── Gestión de Productos
         ├─── Gestión de Carrito
         ├─── Proceso de Compra
         └─── Gestión de Pagos
```

---

## 5.1. CASOS DE USO - GESTIÓN DE USUARIOS

### **CU-001: Registrar Usuario**
**ID:** CU-001  
**Actor:** Usuario Anónimo  
**Precondición:** El usuario no tiene cuenta en el sistema  
**Postcondición:** Se crea una nueva cuenta de usuario

**Flujo Principal:**
1. El usuario selecciona "Registrarse"
2. El sistema muestra formulario de registro
3. El usuario ingresa: nombre, apellido, email, contraseña, confirmar contraseña
4. El usuario envía el formulario
5. El sistema valida que el email sea único
6. El sistema valida que las contraseñas coincidan
7. El sistema crea la cuenta con rol "Cliente"
8. El sistema muestra mensaje de éxito
9. El sistema redirige al login

**Flujos Alternativos:**
- **5a. Email ya existe:** Sistema muestra error "Email ya registrado"
- **6a. Contraseñas no coinciden:** Sistema muestra error "Las contraseñas no coinciden"

---

### **CU-002: Iniciar Sesión**
**ID:** CU-002  
**Actor:** Usuario Registrado  
**Precondición:** El usuario tiene cuenta creada  
**Postcondición:** El usuario accede al sistema autenticado

**Flujo Principal:**
1. El usuario ingresa email y contraseña
2. El usuario hace clic en "Iniciar Sesión"
3. El sistema verifica credenciales
4. El sistema genera token JWT
5. El sistema redirige al dashboard del usuario
6. El sistema actualiza carrito temporal si existe

**Flujos Alternativos:**
- **3a. Credenciales incorrectas:** Sistema muestra error "Email o contraseña incorrectos"

---

### **CU-003: Cerrar Sesión**
**ID:** CU-003  
**Actor:** Usuario Autenticado  
**Precondición:** El usuario tiene sesión activa  
**Postcondición:** La sesión se cierra correctamente

**Flujo Principal:**
1. El usuario selecciona "Cerrar Sesión"
2. El sistema invalida el token JWT
3. El sistema redirige a la página principal
4. El sistema limpia datos de sesión del cliente

---

## 5.2. CASOS DE USO - GESTIÓN DE PRODUCTOS

### **CU-004: Agregar Producto (Admin)**
**ID:** CU-004  
**Actor:** Administrador  
**Precondición:** El administrador tiene sesión activa  
**Postcondición:** Se agrega un nuevo producto al catálogo

**Flujo Principal:**
1. El administrador accede al panel de gestión
2. Selecciona "Agregar Producto"
3. El sistema muestra formulario de producto
4. Administrador ingresa: nombre, descripción, precio, categoría, stock, imagen, especificaciones técnicas
5. Administrador envía el formulario
6. El sistema valida campos requeridos
7. El sistema guarda el producto en base de datos
8. El sistema muestra confirmación de éxito

**Flujos Alternativos:**
- **6a. Campos inválidos:** Sistema muestra errores de validación
- **6b. Precio negativo:** Sistema muestra error "El precio debe ser positivo"

---

### **CU-005: Editar Producto (Admin)**
**ID:** CU-005  
**Actor:** Administrador  
**Precondición:** El producto existe en el sistema  
**Postcondición:** El producto se actualiza correctamente

**Flujo Principal:**
1. Administrador selecciona producto a editar
2. El sistema muestra formulario con datos actuales
3. Administrador modifica campos necesarios
4. Administrador guarda cambios
5. El sistema valida y actualiza el producto
6. El sistema muestra confirmación

---

### **CU-006: Eliminar Producto (Admin)**
**ID:** CU-006  
**Actor:** Administrador  
**Precondición:** El producto existe y no tiene órdenes activas  
**Postcondición:** El producto se elimina del sistema

**Flujo Principal:**
1. Administrador selecciona producto a eliminar
2. El sistema muestra confirmación de eliminación
3. Administrador confirma la acción
4. El sistema elimina el producto
5. El sistema muestra mensaje de éxito

---

### **CU-007: Buscar Productos**
**ID:** CU-007  
**Actor:** Usuario (Autenticado o Anónimo)  
**Precondición:** El sistema tiene productos disponibles  
**Postcondición:** Se muestran productos que coinciden con la búsqueda

**Flujo Principal:**
1. Usuario ingresa término de búsqueda en el buscador
2. El sistema busca productos por nombre y descripción
3. El sistema muestra resultados en tiempo real
4. Usuario puede seleccionar producto para ver detalles

**Flujos Alternativos:**
- **2a. Sin resultados:** Sistema muestra "No se encontraron productos"

---

### **CU-008: Filtrar Productos por Categoría**
**ID:** CU-008  
**Actor:** Usuario (Autenticado o Anónimo)  
**Precondición:** Existen categorías definidas  
**Postcondición:** Se muestran productos de la categoría seleccionada

**Flujo Principal:**
1. Usuario selecciona categoría del filtro
2. El sistema filtra productos por categoría seleccionada
3. El sistema muestra productos de esa categoría
4. Usuario puede navegar entre los resultados

---

### **CU-009: Ver Detalles de Producto**
**ID:** CU-009  
**Actor:** Usuario (Autenticado o Anónimo)  
**Precondición:** El producto existe y está disponible  
**Postcondición:** Se muestran todos los detalles del producto

**Flujo Principal:**
1. Usuario selecciona producto de la lista
2. El sistema carga información completa del producto
3. El sistema muestra: nombre, descripción, precio, imágenes, especificaciones técnicas, stock disponible
4. Usuario puede agregar producto al carrito

---

## 5.3. CASOS DE USO - GESTIÓN DE CARRITO

### **CU-010: Agregar Producto al Carrito**
**ID:** CU-010  
**Actor:** Usuario (Autenticado o Anónimo)  
**Precondición:** El producto existe y tiene stock disponible  
**Postcondición:** El producto se agrega al carrito

**Flujo Principal:**
1. Usuario selecciona "Agregar al Carrito" en un producto
2. El sistema verifica stock disponible
3. El sistema agrega producto al carrito (cantidad: 1)
4. El sistema actualiza contador del carrito en header
5. El sistema muestra mensaje de confirmación

**Flujos Alternativos:**
- **2a. Sin stock:** Sistema muestra error "Producto agotado"
- **2b. Ya en carrito:** Sistema incrementa cantidad si hay stock

---

### **CU-011: Modificar Cantidad en Carrito**
**ID:** CU-011  
**Actor:** Usuario (Autenticado o Anónimo)  
**Precondición:** El producto está en el carrito  
**Postcondición:** Se actualiza la cantidad del producto

**Flujo Principal:**
1. Usuario accede al carrito de compras
2. Usuario modifica cantidad en input numérico
3. El sistema valida que nueva cantidad ≤ stock disponible
4. El sistema actualiza cantidad y recalcula subtotal
5. El sistema actualiza total general del carrito

**Flujos Alternativos:**
- **3a. Stock insuficiente:** Sistema muestra stock máximo disponible
- **3b. Cantidad = 0:** Sistema elimina producto del carrito

---

### **CU-012: Eliminar Producto del Carrito**
**ID:** CU-012  
**Actor:** Usuario (Autenticado o Anónimo)  
**Precondición:** El producto está en el carrito  
**Postcondición:** El producto se elimina del carrito

**Flujo Principal:**
1. Usuario accede al carrito de compras
2. Usuario selecciona "Eliminar" en el producto
3. El sistema muestra confirmación
4. Usuario confirma eliminación
5. El sistema remueve producto del carrito
6. El sistema actualiza total y contador

---

### **CU-013: Ver Carrito de Compras**
**ID:** CU-013  
**Actor:** Usuario (Autenticado o Anónimo)  
**Precondición:** El carrito tiene al menos un producto  
**Postcondición:** Se muestra resumen del carrito

**Flujo Principal:**
1. Usuario selecciona icono del carrito
2. El sistema muestra lista de productos en carrito
3. Para cada producto muestra: imagen, nombre, precio unitario, cantidad, subtotal
4. El sistema muestra total general
5. El sistema muestra botón "Proceder al Checkout"

---

## 5.4. CASOS DE USO - PROCESO DE COMPRA

### **CU-014: Iniciar Checkout**
**ID:** CU-014  
**Actor:** Usuario Autenticado  
**Precondición:** El usuario tiene productos en el carrito  
**Postcondición:** Se inicia el proceso de checkout

**Flujo Principal:**
1. Usuario selecciona "Proceder al Checkout"
2. El sistema valida que carrito no esté vacío
3. El sistema valida stock de todos los productos
4. El sistema muestra formulario de checkout
5. Usuario ingresa: dirección de envío, información de contacto
6. El sistema muestra resumen de compra y total

**Flujos Alternativos:**
- **2a. Carrito vacío:** Sistema redirige a página de productos
- **3a. Stock insuficiente:** Sistema notifica y actualiza carrito

---

### **CU-015: Confirmar Pedido**
**ID:** CU-015  
**Actor:** Usuario Autenticado  
**Precondición:** El usuario completó datos de envío  
**Postcondición:** Se crea la orden de pedido

**Flujo Principal:**
1. Usuario revisa resumen de compra
2. Usuario selecciona método de pago
3. Usuario hace clic en "Confirmar Pedido"
4. El sistema crea orden con estado "Pendiente de pago"
5. El sistema reserva stock de productos
6. El sistema vacía el carrito
7. El sistema redirige a proceso de pago según método seleccionado

---

## 5.5. CASOS DE USO - SISTEMA DE PAGOS

### **CU-016: Pagar con Yape**
**ID:** CU-016  
**Actor:** Usuario Autenticado  
**Precondición:** Existe una orden pendiente de pago  
**Postcondición:** Se genera código QR y se inicia proceso de pago

**Flujo Principal:**
1. Usuario selecciona "Pagar con Yape"
2. El sistema genera código QR con: monto total, número de pedido, información de la empresa
3. El sistema muestra número de celular para transferencia manual
4. El sistema muestra instrucciones para el pago
5. El sistema cambia estado de orden a "Esperando pago Yape"
6. El sistema muestra formulario para subir comprobante

---

### **CU-017: Subir Comprobante Yape**
**ID:** CU-017  
**Actor:** Usuario Autenticado  
**Precondición:** El usuario realizó el pago con Yape  
**Postcondición:** Se sube comprobante para verificación

**Flujo Principal:**
1. Usuario selecciona "Subir Comprobante"
2. El sistema muestra selector de archivos
3. Usuario selecciona archivo (JPG, PNG, PDF)
4. El sistema valida tipo y tamaño (≤5MB)
5. El sistema sube el archivo
6. El sistema muestra preview del comprobante
7. El sistema cambia estado a "Pago pendiente de verificación"
8. El sistema notifica a administradores

**Flujos Alternativos:**
- **4a. Archivo inválido:** Sistema muestra error "Formato no soportado"
- **4b. Archivo muy grande:** Sistema muestra error "Archivo muy grande"

---

### **CU-018: Verificar Pago Yape (Admin)**
**ID:** CU-018  
**Actor:** Administrador  
**Precondición:** Existen órdenes con pago pendiente de verificación  
**Postcondición:** Se verifica el comprobante de pago

**Flujo Principal:**
1. Administrador accede a panel de verificaciones pendientes
2. El sistema muestra lista de órdenes con comprobantes
3. Administrador selecciona orden para verificar
4. El sistema muestra comprobante subido y detalles de la orden
5. Administrador verifica comprobante contra datos de la orden
6. Administrador selecciona "Confirmar Pago" o "Rechazar Pago"
7. El sistema actualiza estado de la orden y notifica al usuario

---

### **CU-019: Confirmar Pago Yape (Admin)**
**ID:** CU-019  
**Actor:** Administrador  
**Precondición:** El comprobante es válido y coincide con la orden  
**Postcondición:** El pago se confirma y la orden avanza

**Flujo Principal:**
1. Administrador selecciona "Confirmar Pago"
2. El sistema cambia estado de orden a "Pago confirmado"
3. El sistema notifica al usuario por email
4. El sistema prepara orden para envío
5. El sistema registra auditoría de la transacción

---

### **CU-020: Rechazar Pago Yape (Admin)**
**ID:** CU-020  
**Actor:** Administrador  
**Precondición:** El comprobante es inválido o no coincide  
**Postcondición:** El pago se rechaza y se notifica al usuario

**Flujo Principal:**
1. Administrador selecciona "Rechazar Pago"
2. El sistema solicita motivo del rechazo
3. Administrador ingresa motivo
4. El sistema cambia estado de orden a "Pago rechazado"
5. El sistema notifica al usuario con el motivo
6. El sistema libera stock reservado
7. El usuario puede intentar el pago nuevamente

---

## 5.6. CASOS DE USO - GESTIÓN DE ÓRDENES

### **CU-021: Ver Historial de Pedidos (Usuario)**
**ID:** CU-021  
**Actor:** Usuario Autenticado  
**Precondición:** El usuario tiene órdenes realizadas  
**Postcondición:** Se muestra historial de pedidos del usuario

**Flujo Principal:**
1. Usuario accede a "Mis Pedidos"
2. El sistema muestra lista de órdenes del usuario
3. Para cada orden muestra: número, fecha, total, estado actual
4. Usuario puede seleccionar orden para ver detalles completos
5. El sistema muestra detalles: productos, cantidades, dirección, historial de estados

---

### **CU-022: Ver Todas las Órdenes (Admin)**
**ID:** CU-022  
**Actor:** Administrador  
**Precondición:** Existen órdenes en el sistema  
**Postcondición:** Se muestran todas las órdenes del sistema

**Flujo Principal:**
1. Administrador accede a "Gestión de Órdenes"
2. El sistema muestra lista completa de órdenes
3. El sistema permite filtrar por estado, fecha, usuario
4. Administrador puede buscar órdenes por número o usuario
5. Administrador puede ver detalles completos de cualquier orden

---

### **CU-023: Actualizar Estado de Orden (Admin)**
**ID:** CU-023  
**Actor:** Administrador  
**Precondición:** La orden existe y tiene pago confirmado  
**Postcondición:** El estado de la orden se actualiza

**Flujo Principal:**
1. Administrador selecciona orden a actualizar
2. El sistema muestra estado actual y opciones disponibles
3. Administrador selecciona nuevo estado (Enviado, Entregado, etc.)
4. El sistema actualiza estado de la orden
5. El sistema registra fecha/hora del cambio
6. El sistema notifica al usuario del cambio de estado

---

## 5.7. CASOS DE USO - SISTEMA DE PAGOS ESCALABLE

### **CU-024: Agregar Nuevo Método de Pago (Sistema)**
**ID:** CU-024  
**Actor:** Sistema/Desarrollador  
**Precondición:** Se requiere integrar nuevo gateway de pago  
**Postcondición:** El nuevo método de pago está disponible

**Flujo Principal:**
1. Desarrollador implementa nueva clase que cumple con PaymentGateway interface
2. Se configuran parámetros del nuevo gateway en sistema
3. Se actualiza base de datos para soportar nuevo método
4. Se actualiza frontend para mostrar nueva opción
5. Se realizan pruebas de integración
6. Se despliega en producción

---

### **CU-025: Procesar Pago con Múltiples Métodos**
**ID:** CU-025  
**Actor:** Sistema  
**Precondición:** Existen múltiples gateways configurados  
**Postcondición:** El pago se procesa según método seleccionado

**Flujo Principal:**
1. Sistema identifica método de pago seleccionado
2. Sistema instancia el gateway correspondiente
3. Sistema ejecuta flujo específico del método
4. Sistema procesa respuesta del gateway
5. Sistema actualiza estado de la orden según resultado
6. Sistema registra transacción en auditoría

---

## 📊 MATRIZ DE ACTORES Y CASOS DE USO

| Actor | Casos de Uso |
|-------|-------------|
| **Usuario Anónimo** | CU-001, CU-007, CU-008, CU-009, CU-010, CU-011, CU-012, CU-013 |
| **Usuario Autenticado** | CU-002, CU-003, CU-007 a CU-017, CU-021 |
| **Administrador** | CU-004, CU-005, CU-006, CU-018, CU-019, CU-020, CU-022, CU-023 |
| **Sistema** | CU-024, CU-025 |

---

## 🔄 RELACIONES ENTRE CASOS DE USO

### **Inclusión (Include):**
- CU-014 incluye CU-013 (Ver carrito antes de checkout)
- CU-016 incluye CU-015 (Confirmar pedido antes de pago)

### **Extensión (Extend):**
- CU-018 extiende CU-022 (Verificación de pagos desde gestión de órdenes)
- CU-023 extiende CU-022 (Actualización de estado desde gestión de órdenes)

### **Herencia:**
- Todos los métodos de pago futuros heredan de CU-025
- CU-019 y CU-020 son especializaciones de CU-018


## 6. ARQUITECTURA DE PAGOS ESCALABLE

### 6.1 Diseño del Sistema de Pagos
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

### 6.2 Flujo de Pago con Yape
1. **Selección de método** → Usuario elige "Pagar con Yape"
2. **Generación de QR** → Sistema crea QR con datos del pedido
3. **Pago usuario** → Usuario escanea y paga con Yape
4. **Subir comprobante** → Usuario sube screenshot del pago
5. **Verificación admin** → Admin valida comprobante
6. **Confirmación** → Sistema actualiza estado del pedido

### 6.3 Preparación para Métodos Futuros
- **Base de datos:** Campos para múltiples gateways
- **API:** Endpoints genéricos para procesamiento de pagos
- **Frontend:** Componente modular para diferentes métodos
- **Backend:** Patrón Strategy para gateways de pago

##7. DEFINICIÓN DE TERMINADO

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