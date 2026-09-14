# 🖥️ FYZ-Tech - Tienda en Línea de Equipos de Cómputo

## 📚 Información del Curso
- **Curso:** Construcción de Software I
- **Profesor:** Rolando Rojas Gallo
- **Ciclo:** 2024-2

## 👥 Integrantes del Equipo
| Nombre | Rol Principal | Responsabilidades |
|--------|---------------|------------------|
| **Yazid** | Desarrollador Backend | Gestión del proyecto, arquitectura backend, base de datos |
| **Franz** | Desarrollador Frontend & UI/UX | Interfaz de usuario, experiencia de usuario, desarrollo frontend |

## 🎯 Objetivo del Proyecto
Desarrollar e implementar una tienda en línea especializada en equipos de cómputo, componentes y accesorios tecnológicos que permita la venta y promoción de productos de forma eficiente, segura y accesible.

## 📋 Índice del Proyecto

### 📁 Estructura del Repositorio
```
tienda-equipos-computo_FYZ-Tech/
│
├── 📁 docs/                          # Documentación del proyecto
│   ├── plan-de-trabajo.md
│   ├── requerimientos.md
│   ├── arquitectura.md
│   ├── decisiones-tecnicas.md
│   ├── wireframes-mockups/
│   └── actas-reuniones/
│
├── 📁 backend/                       # Código del backend
│   └── PC3-CS1/
│       ├── src/
│       ├── pom.xml
│       └── Dockerfile
│
├── 📁 frontend/                      # Código del frontend
│   └── fyztech/
│       ├── src/
│       ├── package.json
│       └── Dockerfile
│
├── 📁 database/                      # Scripts de base de datos
│   ├── esquema.sql
│   └── datos-iniciales.sql
│
├── 📁 scripts/                       # Scripts de utilidad
├── 📁 pruebas/                       # Pruebas del sistema
├── docker-compose.yml               # Orquestación de contenedores
├── .gitignore
└── README.md
```

### 🗓️ Fases del Proyecto
1. **📊 Análisis y Requerimientos**
2. **🎨 Diseño UI/UX y Arquitectura**
3. **⚙️ Desarrollo Backend**
4. **🎪 Desarrollo Frontend**
5. **🧪 Pruebas y Calidad**
6. **🚀 Implementación y Lanzamiento**
7. **📈 Monitoreo y Mantenimiento**

## 🛠️ Stack Tecnológico

### Frontend
- **Angular** - Biblioteca de interfaz de usuario
- **HTML5/CSS3** - Estructura y estilos
- **JavaScript (ES6+)** - Lógica del cliente

### Backend
- **Springboot** - Entorno de ejecución
- **JWT** - Autenticación

### Base de Datos
- **Supabase-PostgreSQL** - Base de datos relacional

### Infraestructura
- **Git/GitHub** - Control de versiones

## 🚀 Configuración y Desarrollo

### Prerrequisitos
- Docker Desktop con Docker Compose
- Git

### Ejecución con Docker
```bash
# Clonar el repositorio (si aún no lo tienes)
git clone https://github.com/francito69/tienda-equipos-computo_FYZ-Tech.git

# Navegar al directorio del proyecto
cd tienda-equipos-computo_FYZ-Tech

# Construir las imágenes y levantar frontend, backend, PostgreSQL y pgAdmin
docker compose up --build -d
```

La aplicación queda disponible en:

- Frontend: http://localhost:4200
- Backend: http://localhost:8080
- pgAdmin: http://localhost:5050

Para ver los logs o detener los servicios:

```bash
docker compose logs -f
docker compose down
```

Los scripts de `database/` se ejecutan únicamente al crear el volumen de
PostgreSQL por primera vez. Para recrear la base de datos desde cero:

```bash
docker compose down -v
docker compose up --build -d
```

## 📞 Contacto y Comunicación
- **Repositorio:** [[GitHub - tienda-equipos-computo_FYZ-Tech](https://github.com/francito69/tienda-equipos-computo_FYZ-Tech)]
- **Correo del equipo:** [ yazid.fernandez.d@uni.pe Y franz.inga.c@uni.pe] 

## 📊 Estado del Proyecto
![Estado](https://img.shields.io/badge/Estado-En%20Desarrollo-yellow)
![Versión](https://img.shields.io/badge/Versión-0.1.0-blue)

## 📝 Licencia
Este proyecto es desarrollado con fines académicos para el curso de Construcción de Software I.

---

**💻 Desarrollado con dedicación por el equipo FYZ-Tech**
