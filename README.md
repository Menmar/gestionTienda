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
| Generación PDF | OpenPDF 1.3.43 |
| API docs | SpringDoc OpenAPI 3.0.3 (Swagger UI) |
| Tests | JUnit 5 + Testcontainers (PostgreSQL) |

## Módulos de negocio

- **Calzado** — tickets de reparación con líneas por tipo de reparación y familia
- **Costura** — tickets de costura (ropa, bolsos, cinturones…)
- **Llaves** — duplicados por tipo de llave
- **Fotos** — adjuntos de imagen (jpg, png, webp, gif) almacenados en disco vinculados a tickets
- **PDF** — resguardo imprimible del ticket con código, tipo, líneas y fecha de entrega
- **Catálogo** — familias y tipos de reparación, costura y llave (escritura solo ADMIN)
- **Clientes** — CRUD con búsqueda por teléfono y listado paginado
- **Usuarios** — empleados y administradores (solo ADMIN)

## Requisitos previos

- Java 25+
- Maven 3.9+
- PostgreSQL 15+ (base de datos `gestion_tienda`)
- Docker (obligatorio para los tests de integración con Testcontainers)

## Configuración

Variables de entorno necesarias en producción:

| Variable | Descripción | Default |
|----------|-------------|---------|
| `DB_USERNAME` | Usuario de PostgreSQL | — |
| `DB_PASSWORD` | Contraseña de PostgreSQL | — |
| `JWT_SECRET` | Clave HMAC ≥ 32 caracteres | — |
| `FOTOS_DIR` | Ruta absoluta para almacenar las fotos | `./uploads/fotos` |

Para desarrollo local usa el perfil `local` (`application-local.yaml`).

## Ejecución local

```bash
./mvnw spring-boot:run -Plocal
```

- API: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

## Tests de integración

```bash
./mvnw test
```

Requiere Docker. Testcontainers levanta un contenedor PostgreSQL automáticamente para cada ejecución.

## Análisis de calidad (SonarQube)

```bash
./mvnw verify sonar:sonar -Psonar
```

Requiere SonarQube corriendo en `http://localhost:9000`.

## Seguridad

- Autenticación stateless con JWT (access token 15 min · refresh token 7 días)
- Los refresh tokens se persisten en base de datos y pueden revocarse individualmente
- Roles: `ADMIN` (gestión de usuarios y catálogo) · `EMPLEADO` (operativa diaria)
- Ficheros subidos: solo se aceptan extensiones `jpg`, `jpeg`, `png`, `webp`, `gif`

## API — resumen de endpoints

### Autenticación
| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/auth/login` | Obtener access + refresh token |
| POST | `/auth/refresh` | Renovar access token con el refresh token |
| POST | `/auth/logout` | Revocar refresh token |

### Tickets
| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/tickets` | Crear ticket |
| GET | `/tickets?page=0&size=20&estado=&tipo=&clienteId=` | Listar paginado con filtros opcionales |
| GET | `/tickets/{id}` | Buscar por id |
| GET | `/tickets/numero/{numero}` | Buscar por código (ej. `LLA-00001`) |
| PATCH | `/tickets/{id}/estado` | Cambiar estado |
| GET | `/tickets/{id}/pdf` | Descargar resguardo PDF |

### Fotos de ticket
| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/tickets/{id}/fotos` | Subir foto (`multipart/form-data`, campo `fichero`) |
| GET | `/tickets/{id}/fotos` | Listar metadatos de fotos |
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

### Usuarios
| Método | Ruta | Acceso | Descripción |
|--------|------|--------|-------------|
| POST | `/usuarios` | ADMIN | Crear usuario con contraseña inicial |
| GET | `/usuarios?page=0&size=20` | ADMIN | Listar paginado |
| GET | `/usuarios/{id}` | ADMIN | Buscar por id |
| PUT | `/usuarios/{id}` | ADMIN | Actualizar datos (sin contraseña) |
| DELETE | `/usuarios/{id}` | ADMIN | Desactivar (soft delete) |
| PATCH | `/usuarios/{id}/password` | ADMIN | Forzar reset de contraseña |
| PATCH | `/usuarios/me/password` | Autenticado | Cambiar propia contraseña (requiere actual) |

### Catálogo *(lectura libre · escritura solo ADMIN)*
| Método | Ruta |
|--------|------|
| GET · POST · PUT · DELETE | `/catalogo/familias` · `/catalogo/familias/{id}` |
| GET · POST · PUT · DELETE | `/catalogo/reparaciones-calzado` · `/catalogo/reparaciones-calzado/{id}` |
| GET · POST · PUT · DELETE | `/catalogo/costuras` · `/catalogo/costuras/{id}` |
| GET · POST · PUT · DELETE | `/catalogo/llaves` · `/catalogo/llaves/{id}` |

## Estructura del proyecto

```
src/main/java/com/menmar/gestionTienda/
├── config/          # SecurityConfig, GlobalExceptionHandler, AppProperties
├── controller/      # REST controllers
├── mapper/          # Interfaces MapStruct (entidad ↔ DTO)
├── model/           # Records de request/response, PageResponse
│   ├── auth/
│   ├── catalogo/
│   ├── cliente/
│   ├── foto/
│   ├── ticket/
│   └── usuario/
├── persistence/
│   ├── entity/      # Entidades JPA + enums de dominio
│   └── repository/  # Spring Data repositories
├── security/        # JwtService, JwtAuthenticationFilter, UserDetailsServiceImpl
└── service/         # Interfaces de servicio + implementaciones (impl/)
```

## Posibles mejoras futuras

### Funcionalidad
- **Historial de estados** — tabla `ticket_estado_log` con marca de tiempo y empleado en cada transición
- **Notificaciones** — email o SMS al cliente cuando el ticket pasa a LISTO
- **Presupuestos** — crear un presupuesto previo vinculable a un ticket
- **Estadísticas** — endpoint de resumen (tickets por estado, ingresos por período, tipos más frecuentes)
- **Búsqueda global** — buscar tickets por nombre de cliente, teléfono o código desde un único endpoint
- **Cambio de contraseña** — endpoint propio para que el empleado cambie su password sin pasar por ADMIN

### Técnicas
- **Refresco automático del access token** — devolver nuevo access token en la cabecera si queda < 2 min de vida
- **Rate limiting** — limitar intentos de login por IP con Bucket4j o Spring Cloud Gateway
- **Caché** — cachear el catálogo (familias, tipos) con Spring Cache + Caffeine; cambia poco y se lee mucho
- **Auditoría** — campos `updatedAt` + `updatedBy` en entidades con Spring Data Envers o `@PreUpdate`
- **Compresión de fotos** — reducir resolución/tamaño antes de guardar en disco
- **Almacenamiento en la nube** — reemplazar el disco local por S3 / Azure Blob Storage
- **OpenAPI anotado** — añadir `@Operation` y `@ApiResponse` en los controllers para enriquecer Swagger UI
- **Dockerfile + docker-compose** — contenerizar la aplicación junto a PostgreSQL para despliegue reproducible
