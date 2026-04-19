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
| Email | Spring Mail (JavaMailSender) |
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
- **Notificaciones** — envío de email/WhatsApp/Telegram al cliente cuando el ticket pasa a LISTO; credenciales configurables por establecimiento en BD (con fallback a variables de entorno globales)

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
| `MAIL_HOST` | Servidor SMTP (ej. `smtp.gmail.com`) | — |
| `MAIL_PORT` | Puerto SMTP | `587` |
| `MAIL_USERNAME` | Cuenta de correo remitente | — |
| `MAIL_PASSWORD` | Contraseña / app-password del correo | — |
| `APP_NOTIFICACIONES_EMAIL_REMITENTE` | From global de email (override por establecimiento en BD) | — |
| `APP_NOTIFICACIONES_CALLMEBOT_API_KEY` | API key global de CallMeBot/WhatsApp (override por establecimiento en BD) | — |
| `APP_NOTIFICACIONES_TELEGRAM_BOT_TOKEN` | Token global del bot de Telegram (override por establecimiento en BD) | — |

Para desarrollo local usa el perfil `local` (`application-local.yaml`).

## Ejecución local

### Opción A — desarrollo con hot-reload (back y front por separado)

Arranca el backend y el frontend en terminales separadas. El proxy de Vite redirige
las llamadas `/api/**` a `localhost:8080`, por lo que no hay que tocar CORS.

```bash
# Terminal 1 — backend (perfil local, recarga con spring-devtools si está en classpath)
./mvnw spring-boot:run -Plocal

# Terminal 2 — frontend (Vite dev server con HMR)
cd frontend
npm install        # solo la primera vez
npm run dev
```

| Servicio | URL |
|----------|-----|
| Frontend (Vite) | `http://localhost:5173` |
| API / Swagger UI | `http://localhost:8080` / `http://localhost:8080/swagger-ui.html` |

### Opción B — JAR integrado (back sirve el front compilado)

Maven compila el frontend y lo copia a `src/main/resources/static` antes de empaquetar el JAR.
Útil para probar el bundle de producción en local.

```bash
./mvnw spring-boot:run -Plocal   # frontend-maven-plugin compila el front automáticamente
```

Accede a todo desde `http://localhost:8080`.

---

## Despliegue en producción

### 1. Variables de entorno obligatorias

Exporta las variables antes de arrancar (o usa un fichero `.env` con tu gestor de procesos):

```bash
export DB_USERNAME=…
export DB_PASSWORD=…
export JWT_SECRET=…                          # clave HMAC ≥ 32 caracteres
export ADMIN_EMAIL=…
export ADMIN_PASSWORD=…

# Correo (SMTP)
export MAIL_HOST=smtp.gmail.com
export MAIL_PORT=587
export MAIL_USERNAME=…
export MAIL_PASSWORD=…                       # app-password de Google u otro proveedor

# Notificaciones globales (opcional si se configura por establecimiento en BD)
export APP_NOTIFICACIONES_EMAIL_REMITENTE=…
export APP_NOTIFICACIONES_CALLMEBOT_API_KEY=…
export APP_NOTIFICACIONES_TELEGRAM_BOT_TOKEN=…

# Almacenamiento de fotos
export FOTOS_DIR=/var/gestiontienda/fotos    # ruta absoluta con permisos de escritura
```

### 2. Generar el JAR de producción

El plugin `frontend-maven-plugin` descarga Node, instala dependencias y ejecuta `npm run build`
automáticamente durante `mvn package`. No es necesario construir el frontend por separado.

```bash
./mvnw package -Pprod -DskipTests
```

El artefacto resultante es `target/gestionTienda-0.0.1-SNAPSHOT.jar` (~50 MB, self-contained).

### 3. Arrancar el servidor

```bash
java -jar target/gestionTienda-0.0.1-SNAPSHOT.jar
```

Spring Boot sirve el frontend compilado en `/` y la API en `/api/**`. No se necesita Nginx
ni servidor web adicional salvo que quieras terminación TLS o reverse proxy.

| Ruta | Contenido |
|------|-----------|
| `/` | Aplicación Vue |
| `/api/**` | REST API |
| `/swagger-ui.html` | Documentación OpenAPI |

### 4. Reverse proxy con Nginx (opcional, para TLS)

```nginx
server {
    listen 443 ssl;
    server_name tudominio.com;

    ssl_certificate     /etc/ssl/certs/tudominio.crt;
    ssl_certificate_key /etc/ssl/private/tudominio.key;

    location / {
        proxy_pass         http://127.0.0.1:8080;
        proxy_set_header   Host $host;
        proxy_set_header   X-Real-IP $remote_addr;
        proxy_set_header   X-Forwarded-Proto https;
    }
}
```

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
├── service/         # Interfaces de servicio + implementaciones (impl/)
│   └── notificacion/ # Estrategias de notificación por canal (EMAIL, …)
└── (raíz)
```

## Posibles mejoras futuras

### Funcionalidad
- **Historial de estados** — tabla `ticket_estado_log` con marca de tiempo y empleado en cada transición
- ~~**Notificaciones** — email o SMS al cliente cuando el ticket pasa a LISTO~~ *(implementado)*
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
