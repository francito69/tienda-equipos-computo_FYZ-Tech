# 🧪 **CASOS DE PRUEBA COMPLETOS EN POSTMAN - SISTEMA DE PAGOS YAPE**

## 📁 **COLLECTION: Sistema Pagos Yape**

### **🔐 1. AUTHENTICATION**
```
📂 Auth
├── 🔓 POST Registrar Usuario
├── 🔓 POST Login
└── 🔓 POST Login Admin
```

### **💰 2. PAGOS YAPE**
```
📂 Pagos Yape
├── ❤️ GET Health Check
├── 🎯 POST Crear Pago con QR
├── 📤 POST Subir Comprobante
├── 🔍 GET Consultar Pago por Orden
├── 👀 GET Pagos Pendientes (Admin)
└── ✅ POST Verificar Pago (Admin)
```

### **🛒 3. ÓRDENES (Prerequisitos)**
```
📂 Órdenes
├── POST Crear Orden
├── GET Obtener Órdenes
└── GET Obtener Orden por ID
```

---

## 📋 **DETALLE DE CASOS DE PRUEBA**

### **🔐 1. REGISTRAR USUARIO**
```http
POST http://localhost:8080/api/auth/registro
Content-Type: application/json
```

**Body:**
```json
{
    "email": "cliente@test.com",
    "contraseña": "password123",
    "nombres": "Juan",
    "apellidos": "Pérez"
}
```

**Respuesta Esperada:**
```json
{
    "id": "uuid",
    "email": "cliente@test.com",
    "nombres": "Juan",
    "apellidos": "Pérez",
    "rol": "CLIENTE"
}
```

**Casos de Prueba:**
1. ✅ Registro exitoso
2. ❌ Email ya existe
3. ❌ Campos requeridos faltantes
4. ❌ Email inválido

---

### **🔐 2. LOGIN USUARIO NORMAL**
```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json
```

**Body:**
```json
{
    "email": "cliente@test.com",
    "contraseña": "password123"
}
```

**Respuesta Esperada:**
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "tipo": "Bearer",
    "email": "cliente@test.com",
    "nombres": "Juan",
    "rol": "CLIENTE"
}
```

**📝 IMPORTANTE:** Guarda este token para los requests siguientes

---

### **🔐 3. LOGIN ADMIN (para pruebas de verificación)**
```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json
```

**Body:**
```json
{
    "email": "admin@tienda.com",
    "contraseña": "admin123"
}
```

**Nota:** Si no existe, crea el admin primero:
```json
{
    "email": "admin@tienda.com",
    "contraseña": "admin123",
    "nombres": "Admin",
    "apellidos": "Sistema",
    "rol": "ADMIN"
}
```

---

### **❤️ 4. HEALTH CHECK**
```http
GET http://localhost:8080/api/pagos/health
Authorization: Bearer {token_del_paso_2}
```

**Respuesta Esperada:**
```
Sistema de pagos Yape operativo ✅
```

**Casos:**
1. ✅ Con token válido
2. ❌ Sin token → 403
3. ❌ Token expirado → 403

---

## 🛒 **PRIMERO: CREAR UNA ORDEN PARA PAGAR**

### **5. CREAR ORDEN**
```http
POST http://localhost:8080/api/ordenes
Authorization: Bearer {token_cliente}
Content-Type: application/json
```

**Body:**
```json
{
    "direccionEnvio": "Av. Ejemplo 123, Lima",
    "metodoPago": "YAPE"
}
```

**Respuesta Esperada:**
```json
{
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "montoTotal": 150.00,
    "estado": "PENDIENTE_PAGO",
    "direccionEnvio": "Av. Ejemplo 123, Lima",
    "fechaCreacion": "2024-01-15T10:30:00",
    "items": []
}
```

**Guarda el `id` de la orden para los siguientes tests**

---

## 🎯 **CASOS DE PRUEBA PAGOS YAPE**

### **Caso 1: ✅ Flujo Normal Exitoso**

#### **6. CREAR PAGO CON QR**
```http
POST http://localhost:8080/api/pagos/orden/{ordenId}/crear-con-qr?qrImageName=mi_yape_qr.png
Authorization: Bearer {token_cliente}
```

**Respuesta Esperada (200 OK):**
```json
{
    "qrImageUrl": "/uploads/qr/mi_yape_qr.png",
    "monto": 150.00,
    "concepto": "Orden #550e8400",
    "numeroCelular": "987126753",
    "instrucciones": "1. Abre Yape y ve a 'Pagar'..."
}
```

#### **7. CONSULTAR PAGO CREADO**
```http
GET http://localhost:8080/api/pagos/orden/{ordenId}
Authorization: Bearer {token_cliente}
```

**Respuesta Esperada:**
```json
{
    "id": "pago-id-uuid",
    "metodoPago": "YAPE",
    "monto": 150.00,
    "estado": "PENDIENTE",
    "qrImageUrl": "/uploads/qr/mi_yape_qr.png",
    "comprobanteImageUrl": null,
    "fechaCreacion": "2024-01-15T10:35:00",
    "ordenId": "550e8400-e29b-41d4-a716-446655440000",
    "ordenEstado": "PENDIENTE_PAGO"
}
```

#### **8. SUBIR COMPROBANTE**
```http
POST http://localhost:8080/api/pagos/orden/{ordenId}/subir-comprobante
Authorization: Bearer {token_cliente}
Content-Type: multipart/form-data
```

**Body (form-data):**
- Key: `comprobante` → File (seleccionar imagen .jpg/.png)

**Respuesta Esperada (200 OK):**
```json
{
    "id": "pago-id-uuid",
    "metodoPago": "YAPE",
    "monto": 150.00,
    "estado": "PENDIENTE_VERIFICACION",
    "qrImageUrl": "/uploads/qr/mi_yape_qr.png",
    "comprobanteImageUrl": "/uploads/comprobantes/comprobante_orden_123.jpg",
    "fechaCreacion": "2024-01-15T10:35:00",
    "ordenId": "550e8400-e29b-41d4-a716-446655440000",
    "ordenEstado": "PENDIENTE_PAGO"
}
```

#### **9. ADMIN: VER PAGOS PENDIENTES**
```http
GET http://localhost:8080/api/pagos/pendientes
Authorization: Bearer {token_admin}
```

**Respuesta Esperada:**
```json
[
    {
        "id": "pago-id-uuid",
        "metodoPago": "YAPE",
        "monto": 150.00,
        "estado": "PENDIENTE_VERIFICACION",
        "qrImageUrl": "/uploads/qr/mi_yape_qr.png",
        "comprobanteImageUrl": "/uploads/comprobantes/comprobante_orden_123.jpg",
        "ordenId": "550e8400-e29b-41d4-a716-446655440000",
        "ordenEstado": "PENDIENTE_PAGO"
    }
]
```

#### **10. ADMIN: VERIFICAR PAGO (APROBAR)**
```http
POST http://localhost:8080/api/pagos/{pagoId}/verificar
Authorization: Bearer {token_admin}
Content-Type: application/json
```

**Body:**
```json
{
    "aprobado": true,
    "observaciones": "Comprobante válido, pago verificado"
}
```

**Respuesta Esperada:**
```json
{
    "id": "pago-id-uuid",
    "metodoPago": "YAPE",
    "monto": 150.00,
    "estado": "VERIFICADO",
    "qrImageUrl": "/uploads/qr/mi_yape_qr.png",
    "comprobanteImageUrl": "/uploads/comprobantes/comprobante_orden_123.jpg",
    "fechaCreacion": "2024-01-15T10:35:00",
    "ordenId": "550e8400-e29b-41d4-a716-446655440000",
    "ordenEstado": "CONFIRMADA"
}
```

#### **11. VERIFICAR ORDEN ACTUALIZADA**
```http
GET http://localhost:8080/api/ordenes/{ordenId}
Authorization: Bearer {token_cliente}
```

**Respuesta Esperada:**
```json
{
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "montoTotal": 150.00,
    "estado": "CONFIRMADA", // ← Cambió de PENDIENTE_PAGO a CONFIRMADA
    "direccionEnvio": "Av. Ejemplo 123, Lima",
    "fechaCreacion": "2024-01-15T10:30:00"
}
```

---

### **Caso 2: ❌ Flujo con Rechazo**

#### **12. ADMIN: RECHAZAR PAGO**
```http
POST http://localhost:8080/api/pagos/{pagoId}/verificar
Authorization: Bearer {token_admin}
Content-Type: application/json
```

**Body:**
```json
{
    "aprobado": false,
    "observaciones": "Comprobante no coincide con el monto"
}
```

**Respuesta Esperada:**
```json
{
    "id": "pago-id-uuid",
    "metodoPago": "YAPE",
    "monto": 150.00,
    "estado": "RECHAZADO",
    "qrImageUrl": "/uploads/qr/mi_yape_qr.png",
    "comprobanteImageUrl": "/uploads/comprobantes/comprobante_orden_123.jpg",
    "fechaCreacion": "2024-01-15T10:35:00",
    "ordenId": "550e8400-e29b-41d4-a716-446655440000",
    "ordenEstado": "PAGO_RECHAZADO"
}
```

---

## 🚨 **CASOS DE ERROR - PRUEBAS NEGATIVAS**

### **Caso 3: ❌ Orden no existe**
```http
POST http://localhost:8080/api/pagos/orden/00000000-0000-0000-0000-000000000000/crear-con-qr?qrImageName=mi_yape_qr.png
Authorization: Bearer {token_cliente}
```

**Respuesta Esperada:** `400 Bad Request`
```json
"Error: Orden no encontrada"
```

### **Caso 4: ❌ QR no existe en servidor**
```http
POST http://localhost:8080/api/pagos/orden/{ordenId}/crear-con-qr?qrImageName=qr_inexistente.png
Authorization: Bearer {token_cliente}
```

**Respuesta:** El pago se crea, pero la imagen QR no existirá en `/uploads/qr/`

### **Caso 5: ❌ Ya existe pago para la orden**
```http
POST http://localhost:8080/api/pagos/orden/{ordenId}/crear-con-qr?qrImageName=mi_yape_qr.png
Authorization: Bearer {token_cliente}
```

**Respuesta Esperada:** `400 Bad Request`
```json
"Error: Ya existe un pago para esta orden"
```

### **Caso 6: ❌ Archivo inválido al subir comprobante**
```http
POST http://localhost:8080/api/pagos/orden/{ordenId}/subir-comprobante
Authorization: Bearer {token_cliente}
Content-Type: multipart/form-data
```

**Archivo:**
- `.exe` o `.txt` (formato no permitido)
- Archivo vacío
- Archivo > 5MB

**Respuesta Esperada:** `400 Bad Request`
```json
"Error: Solo se permiten imágenes JPG, PNG o PDF"
```

### **Caso 7: ❌ Usuario normal intenta verificar pago**
```http
POST http://localhost:8080/api/pagos/{pagoId}/verificar
Authorization: Bearer {token_cliente}  // ← Token de cliente, no admin
Content-Type: application/json
```

**Respuesta Esperada:** `403 Forbidden`

### **Caso 8: ❌ Sin autenticación**
```http
POST http://localhost:8080/api/pagos/orden/{ordenId}/crear-con-qr?qrImageName=mi_yape_qr.png
// Sin header Authorization
```

**Respuesta Esperada:** `403 Forbidden`

---

## 🧪 **TESTS DE STRESS/PERFORMANCE**

### **Caso 9: Múltiples pagos simultáneos**
1. Crear 5 órdenes diferentes
2. Para cada orden, crear pago con QR
3. Subir comprobantes en paralelo

### **Caso 10: Reintentos**
1. Subir comprobante → falla (simular timeout)
2. Reintentar 3 veces
3. Verificar que no se dupliquen archivos

---

## 📊 **VERIFICACIONES POST-PRUEBA**

### **1. Base de Datos:**
```sql
-- Ver todos los pagos
SELECT * FROM pagos;

-- Ver estados
SELECT estado, COUNT(*) as cantidad FROM pagos GROUP BY estado;

-- Ver relación orden-pago
SELECT o.id as orden_id, o.estado as orden_estado, 
       p.id as pago_id, p.estado as pago_estado
FROM ordenes o 
LEFT JOIN pagos p ON o.id = p.orden_id;
```

### **2. Sistema de Archivos:**
```
uploads/
├── qr/
│   └── mi_yape_qr.png
└── comprobantes/
    ├── comprobante_orden_xxx_1234567890.jpg
    └── comprobante_orden_yyy_1234567891.jpg
```

### **3. Logs del Servidor:**
Verificar que aparecen los logs:
- `✅ Pago creado`
- `✅ Comprobante subido`
- `✅ Pago VERIFICADO`
- `❌ Errores de validación`

---

## 🎯 **SCRIPT DE PRUEBAS AUTOMATIZADO**

### **Postman Collection Runner:**
1. Crear environment variables:
   ```
   baseUrl: http://localhost:8080
   clienteToken: {{token_cliente}}
   adminToken: {{token_admin}}
   ordenId: 
   pagoId:
   ```

2. Secuencia de ejecución:
   ```
   1. Login cliente → guardar clienteToken
   2. Crear orden → guardar ordenId
   3. Crear pago QR
   4. Subir comprobante
   5. Login admin → guardar adminToken
   6. Verificar pago → guardar pagoId
   7. Validar estados finales
   ```

---

## 📝 **CHECKLIST DE PRUEBAS**

- [ ] Registro y login funcionan
- [ ] Health check responde
- [ ] Crear orden exitoso
- [ ] Crear pago con QR
- [ ] Consultar pago por orden
- [ ] Subir comprobante válido
- [ ] Subir comprobante inválido (error)
- [ ] Pago duplicado (error)
- [ ] Orden no existe (error)
- [ ] Admin ve pagos pendientes
- [ ] Admin aprueba pago
- [ ] Admin rechaza pago
- [ ] Cliente no puede verificar (error 403)
- [ ] Estados se actualizan correctamente
- [ ] Archivos se guardan en uploads/
- [ ] QR se muestra correctamente

**¡Listo!** Con estos casos de prueba puedes validar completamente tu sistema de pagos Yape. ¿Necesitas algún caso específico adicional? 🧪🚀