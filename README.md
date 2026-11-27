# Java-SpringBoot-WebFlux

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.1-green.svg)](https://spring.io/projects/spring-boot)
[![WebFlux](https://img.shields.io/badge/Spring-WebFlux-blue.svg)](https://docs.spring.io/spring-framework/reference/web/webflux.html)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14%2B-blue.svg)](https://www.postgresql.org/)

API REST de ejemplo construida con **Java**, **Spring WebFlux** y **Project Reactor**. Implementa operaciones CRUD para tareas, productos y órdenes, con persistencia reactiva en PostgreSQL mediante **Spring Data R2DBC** y una arquitectura en capas.

---

## 🚀 Características principales

- **API reactiva:** endpoints no bloqueantes con `Mono<T>` y `Flux<T>`.
- **Persistencia PostgreSQL:** acceso reactivo a datos con Spring Data R2DBC.
- **Arquitectura en capas:** controladores, servicios, repositorios y modelos de dominio.
- **CRUD:** operaciones de creación, consulta, actualización y eliminación para tareas, productos y órdenes.
- **JSON:** los nombres de propiedades de los modelos Java se exponen en `camelCase`.
- **Autenticación por API key:** las rutas `/api/v1/*` requieren el encabezado `X-API-Key`.
- **Validación de estilo:** Checkstyle integrado en Maven y ejecutado en la fase `validate`.

---

## 🛠️ Stack tecnológico

| Componente | Tecnología |
| :--- | :--- |
| Lenguaje | Java 21 |
| Framework HTTP | Spring WebFlux |
| Runtime reactivo | Project Reactor |
| Base de datos | PostgreSQL 14+ |
| Acceso a datos | Spring Data R2DBC |
| Build | Maven |
| Calidad de código | Checkstyle 8.42 |
| Documentación API | Springdoc OpenAPI 3.2.0 |

---

## 📂 Arquitectura del proyecto

```text
Java-SpringBoot-WebFlux/
├── pom.xml
├── README.md
├── checkstyle.xml
└── src/main/
    ├── java/com/example/spring/
    │   ├── config/       # R2DBC, autenticación y Swagger
    │   ├── domain/       # Entidades y DTOs de tareas, productos y órdenes
    │   ├── repository/   # Repositorios ReactiveCrudRepository
    │   ├── service/      # Lógica de negocio reactiva
    │   └── web/          # Controladores REST WebFlux
    └── resources/
        ├── application.yml
        ├── schema.sql
        └── data.sql
```

---

## 📡 Endpoints

El servidor usa el puerto definido por `PORT` (por defecto, `3000`). Las rutas CRUD están bajo `/api/v1` y requieren `X-API-Key`. `GET /` es público y responde `Server is alive`.

| Recurso | Método | Ruta | Descripción |
| :--- | :--- | :--- | :--- |
| Tareas | `GET` | `/api/v1/task` | Listar tareas |
| Tareas | `GET` | `/api/v1/task/{id}` | Obtener una tarea |
| Tareas | `POST` | `/api/v1/task` | Crear una tarea |
| Tareas | `PUT` | `/api/v1/task` | Actualizar una tarea |
| Tareas | `DELETE` | `/api/v1/task/{id}` | Eliminar una tarea |
| Productos | `GET` | `/api/v1/product` | Listar productos |
| Productos | `GET` | `/api/v1/product/{id}` | Obtener un producto |
| Productos | `POST` | `/api/v1/product` | Crear un producto |
| Productos | `PUT` | `/api/v1/product` | Actualizar un producto |
| Productos | `DELETE` | `/api/v1/product/{id}` | Eliminar un producto |
| Órdenes | `GET` | `/api/v1/order` | Listar órdenes |
| Órdenes | `GET` | `/api/v1/order/{id}` | Obtener una orden |
| Órdenes | `POST` | `/api/v1/order` | Crear una orden |
| Órdenes | `PUT` | `/api/v1/order/{id}` | Actualizar una orden |
| Órdenes | `DELETE` | `/api/v1/order/{id}` | Eliminar una orden |

Las búsquedas, actualizaciones y eliminaciones de registros inexistentes responden `404 Not Found`. Los datos de entrada no válidos responden `400 Bad Request`; las llamadas sin API key válida responden `401 Unauthorized`; los errores no gestionados, incluidos los de base de datos, responden `500 Internal Server Error`.

### Ejemplos de cuerpos JSON

Crear una tarea:

```json
{
  "name": "Revisar el proyecto",
  "done": false
}
```

Actualizar una tarea con `PUT /api/v1/task`:

```json
{
  "taskId": "b92c8a47-07be-4c62-9f22-278b8293e415",
  "name": "Revisar el proyecto",
  "done": true
}
```

Crear un producto:

```json
{
  "name": "Teclado",
  "price": 49.99,
  "stock": 12
}
```

Actualizar un producto con `PUT /api/v1/product`:

```json
{
  "productId": "c5eebff4-b83e-4e16-b9f0-d8efeb0f0641",
  "name": "Teclado",
  "price": 49.99,
  "stock": 12
}
```

Crear una orden:

```json
{
  "name": "Compra de oficina",
  "amount": 149.97,
  "discount": 10.00
}
```

Para actualizar una orden, envía `orderId`, `name`, `amount` y `discount` en el cuerpo de `PUT /api/v1/order/{id}`. El `orderId` del cuerpo debe coincidir con el UUID de la ruta.

---

## ⚙️ Inicio rápido

### Requisitos

- JDK 21.
- Maven.
- Docker, o una instancia local de PostgreSQL 14 o superior.

### 1. Iniciar PostgreSQL

```bash
docker run --name spring-webflux-postgres \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=example \
  -p 5432:5432 \
  -d postgres:14
```

La aplicación ejecuta `schema.sql` y `data.sql` al iniciar. El usuario de la base de datos necesita permisos para crear tablas y habilitar la extensión PostgreSQL `uuid-ossp`. Los datos de ejemplo se insertan sin borrar registros existentes.

### 2. Configurar variables de entorno

La aplicación importa automáticamente el archivo `.env` desde la raíz del proyecto. Copia el ejemplo y genera una clave aleatoria:

```bash
cp .env.example .env
sed -i "s/replace_with_generated_key/$(openssl rand -hex 32)/" .env
```

También puedes editar `.env` manualmente. `API_KEY` es obligatoria y la aplicación no inicia si falta o está vacía. El archivo `.env` está excluido de Git; no compartas su contenido. Para producción, configura las variables en el entorno de ejecución y usa HTTPS.

### 3. Checkstyle, compilación e instalación

```bash
mvn checkstyle:check
mvn install
```

Checkstyle también se ejecuta automáticamente durante `mvn install`. Surefire está configurado para omitir la ejecución de pruebas en este proyecto.

### 4. Ejecutar la aplicación

```bash
mvn spring-boot:run
```

Ejecuta `mvn spring-boot:run` desde la raíz del proyecto para verificar el arranque de la aplicación con Maven de forma simple. Spring Boot cargará automáticamente la configuración del archivo `.env`. La API estará disponible en `http://localhost:3000` (o en el puerto indicado por `PORT`).

---

## 🧪 Prueba rápida

```bash
curl http://localhost:3000/api/v1/task \
  -H "X-API-Key: $API_KEY"
```

Crear una tarea:

```bash
curl -X POST http://localhost:3000/api/v1/task \
  -H 'Content-Type: application/json' \
  -H "X-API-Key: $API_KEY" \
  -d '{"name":"Primera tarea","done":false}'
```

---

## 📖 Swagger UI

La interfaz Swagger UI está disponible en:

```text
http://localhost:3000/swagger-ui.html
```

Solo las rutas `/api/v1/*` requieren la API key; la raíz y la documentación Swagger son públicas.
