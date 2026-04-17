# gestionTienda

Sistema de gestión para una tienda de reparación de calzado y duplicado de llaves.

## Stack

| Capa | Tecnología |
|------|------------|
| Lenguaje | Java 25 |
| Framework | Spring Boot 4.0.5 |
| Persistencia | Spring Data JPA + PostgreSQL |
| Migraciones | Liquibase |
| Seguridad | Spring Security + JWT (JJWT 0.12.6) |
| Mapeos | MapStruct 1.6.3 |
| Boilerplate | Lombok |
| API docs | SpringDoc OpenAPI 3.0.3 (Swagger UI) |
| Tests | JUnit 5 + Testcontainers (PostgreSQL) |

## Módulos de negocio

- **Calzado** — tickets de reparación con líneas por tipo de reparación y familia
- **Costura** — tickets de costura (ropa, bolsos, cinturones…)
- **Llaves** — duplicados por tipo de llave
- **Fotos** — adjuntos almacenados en disco vinculados a tickets
- **Catálogo** — gestión de familias, tipos de reparación, costura y llave (solo ADMIN)
- **Clientes** — CRUD con búsqueda por teléfono
- **Usuarios** — empleados y administradores (solo ADMIN)

## Requisitos previos

- Java 25+
- Maven 3.9+
- PostgreSQL 15+ (base de datos `gestion_tienda`)
- (Opcional) Docker para Testcontainers

## Configuración

Las siguientes variables de entorno son necesarias en producción:

| Variable | Descripción |
|----------|-------------|
| `DB_USERNAME` | Usuario de PostgreSQL |
| `DB_PASSWORD` | Contraseña de PostgreSQL |
| `JWT_SECRET` | Clave HMAC ≥ 32 caracteres |
| `FOTOS_DIR` | Ruta absoluta donde guardar las fotos (default: `./uploads/fotos`) |

Para desarrollo local existe el perfil `local` con valores en `application-local.yaml`.

## Ejecución local

```bash
./mvnw spring-boot:run -Plocal
```

La API arranca en `http://localhost:8080`.  
Swagger UI disponible en `http://localhost:8080/swagger-ui.html`.

## Análisis de calidad (SonarQube)

```bash
./mvnw verify sonar:sonar -Psonar
```

Requiere SonarQube corriendo en `http://localhost:9000`.

## Tests de integración

```bash
./mvnw test
```

Los tests usan Testcontainers: arrancan un contenedor PostgreSQL automáticamente (requiere Docker).

## API — resumen de endpoints

### Autenticación
| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/auth/login` | Obtener access + refresh token |
| POST | `/auth/refresh` | Renovar access token |
| POST | `/auth/logout` | Revocar token |

### Tickets
| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/tickets` | Crear ticket |
| GET | `/tickets?page=0&size=20&estado=&tipo=&clienteId=` | Listar paginado con filtros |
| GET | `/tickets/{id}` | Buscar por id |
| GET | `/tickets/numero/{numero}` | Buscar por número |
| PATCH | `/tickets/{id}/estado` | Cambiar estado |

### Fotos de ticket
| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/tickets/{id}/fotos` | Subir foto (multipart) |
| GET | `/tickets/{id}/fotos` | Listar fotos |
| GET | `/tickets/{id}/fotos/{fotoId}` | Descargar foto |
| DELETE | `/tickets/{id}/fotos/{fotoId}` | Eliminar foto |

### Clientes
| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/clientes` | Crear cliente |
| GET | `/clientes?page=0&size=20` | Listar paginado |
| GET | `/clientes/{id}` | Buscar por id |
| GET | `/clientes/telefono/{telefono}` | Buscar por teléfono |
| PUT | `/clientes/{id}` | Actualizar |

### Usuarios *(solo ADMIN)*
| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/usuarios` | Crear usuario |
| GET | `/usuarios?page=0&size=20` | Listar paginado |
| GET | `/usuarios/{id}` | Buscar por id |
| PUT | `/usuarios/{id}` | Actualizar |
| DELETE | `/usuarios/{id}` | Desactivar |

### Catálogo *(lectura libre, escritura solo ADMIN)*
| Método | Ruta |
|--------|------|
| GET/POST/PUT/DELETE | `/catalogo/familias` |
| GET/POST/PUT/DELETE | `/catalogo/reparaciones-calzado` |
| GET/POST/PUT/DELETE | `/catalogo/costuras` |
| GET/POST/PUT/DELETE | `/catalogo/llaves` |

## Estructura del proyecto

```
src/main/java/com/menmar/gestionTienda/
├── config/          # SecurityConfig, GlobalExceptionHandler, AppProperties
├── controller/      # REST controllers
├── mapper/          # MapStruct interfaces
├── model/           # Records de request/response y PageResponse
├── persistence/
│   ├── entity/      # Entidades JPA + enums
│   └── repository/  # Spring Data repositories
├── security/        # JwtService, JwtAuthenticationFilter
└── service/         # Interfaces + implementaciones
```
