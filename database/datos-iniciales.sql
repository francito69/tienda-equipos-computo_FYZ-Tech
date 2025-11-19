-- Insertar categorías
INSERT INTO categorias (nombre, descripcion) VALUES
('Laptops', 'Computadoras portátiles para trabajo y gaming'),
('Componentes', 'Procesadores, tarjetas gráficas, memorias RAM'),
('Periféricos', 'Teclados, mouse, monitores, audífonos'),
('Accesorios', 'Cables, adaptadores, soportes, fundas');

-- Insertar usuarios (contraseña: "password123" encriptada con bcrypt)
INSERT INTO usuarios (email, contraseña, nombres, apellidos, rol) VALUES
('admin@fytech.com', '$2a$10$N7B7eH5Q7eQ7eQ7eQ7eQ7eOQ7eQ7eQ7eQ7eQ7eQ7eQ7eQ7eQ7eQ', 'Franz', 'Admin', 'ADMIN'),
('cliente@ejemplo.com', '$2a$10$N7B7eH5Q7eQ7eQ7eQ7eQ7eOQ7eQ7eQ7eQ7eQ7eQ7eQ7eQ7eQ7eQ', 'Juan', 'Pérez', 'CLIENTE');

-- Insertar productos
INSERT INTO productos (nombre, descripcion, precio, stock, categoria_id, especificaciones) VALUES
('Laptop Gaming ASUS TUF', 'Laptop gaming con RTX 4060 y procesador Intel i7', 3499.99, 10, 
 (SELECT id FROM categorias WHERE nombre = 'Laptops'),
 '{"procesador": "Intel i7-13700H", "ram": "16GB DDR5", "almacenamiento": "1TB SSD", "pantalla": "15.6\" FHD 144Hz"}'),

('Laptop Dell XPS 13', 'Laptop ultra delgada para profesionales', 2899.99, 8,
 (SELECT id FROM categorias WHERE nombre = 'Laptops'),
 '{"procesador": "Intel i5-1335U", "ram": "8GB LPDDR5", "almacenamiento": "512GB SSD", "pantalla": "13.4\" FHD+"}'),

('RTX 4070 Ti 12GB', 'Tarjeta gráfica para gaming en 4K', 2599.99, 5,
 (SELECT id FROM categorias WHERE nombre = 'Componentes'),
 '{"memoria": "12GB GDDR6X", "conectores": "3x DisplayPort, 1x HDMI", "consumo": "285W"}'),

('Teclado Mecánico Redragon', 'Teclado mecánico RGB switches red', 199.99, 20,
 (SELECT id FROM categorias WHERE nombre = 'Periféricos'),
 '{"tipo": "Mecánico", "switches": "Red", "iluminacion": "RGB", "conectividad": "USB"}'),

('Mouse Logitech G502', 'Mouse gaming con sensor HERO 25K', 149.99, 15,
 (SELECT id FROM categorias WHERE nombre = 'Periféricos'),
 '{"dpi": "25600", "botones": "11", "peso": "121g", "conectividad": "USB"}'),

('Monitor Samsung 27" 4K', 'Monitor UHD para trabajo y gaming', 899.99, 7,
 (SELECT id FROM categorias WHERE nombre = 'Periféricos'),
 '{"tamaño": "27 pulgadas", "resolucion": "3840x2160", "frecuencia": "60Hz", "panel": "IPS"}');