-- Crear tabla de usuarios
CREATE TABLE usuarios (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) UNIQUE NOT NULL,
    contraseña VARCHAR(255) NOT NULL,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    rol VARCHAR(20) DEFAULT 'CLIENTE',
    fecha_creacion TIMESTAMP DEFAULT NOW(),
    fecha_actualizacion TIMESTAMP DEFAULT NOW()
);

-- Crear tabla de categorías
CREATE TABLE categorias (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nombre VARCHAR(100) UNIQUE NOT NULL,
    descripcion TEXT,
    fecha_creacion TIMESTAMP DEFAULT NOW()
);

-- Crear tabla de productos
CREATE TABLE productos (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10,2) NOT NULL,
    stock INTEGER DEFAULT 0,
    imagen_url VARCHAR(500),
    especificaciones JSONB,
    activo BOOLEAN DEFAULT TRUE,
    categoria_id UUID REFERENCES categorias(id),
    fecha_creacion TIMESTAMP DEFAULT NOW(),
    fecha_actualizacion TIMESTAMP DEFAULT NOW()
);

-- Crear tabla de órdenes
CREATE TABLE ordenes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    usuario_id UUID REFERENCES usuarios(id) NOT NULL,
    monto_total DECIMAL(10,2) NOT NULL,
    estado VARCHAR(50) DEFAULT 'PENDIENTE',
    direccion_envio JSONB NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT NOW(),
    fecha_actualizacion TIMESTAMP DEFAULT NOW()
);

-- Crear tabla de items de orden
CREATE TABLE items_orden (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    orden_id UUID REFERENCES ordenes(id) NOT NULL,
    producto_id UUID REFERENCES productos(id) NOT NULL,
    cantidad INTEGER NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL
);

-- Crear tabla de pagos
CREATE TABLE pagos (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    orden_id UUID REFERENCES ordenes(id) UNIQUE NOT NULL,
    metodo_pago VARCHAR(50) NOT NULL,
    monto DECIMAL(10,2) NOT NULL,
    estado VARCHAR(50) DEFAULT 'PENDIENTE',
    qr_code_url VARCHAR(500),
    comprobante_url VARCHAR(500),
    datos_transaccion JSONB,
    fecha_creacion TIMESTAMP DEFAULT NOW(),
    fecha_actualizacion TIMESTAMP DEFAULT NOW()
);

-- Índices para mejor rendimiento
CREATE INDEX idx_productos_categoria ON productos(categoria_id);
CREATE INDEX idx_productos_activo ON productos(activo);
CREATE INDEX idx_ordenes_usuario ON ordenes(usuario_id);
CREATE INDEX idx_ordenes_estado ON ordenes(estado);
CREATE INDEX idx_pagos_estado ON pagos(estado);