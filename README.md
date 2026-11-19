# Java-SpringBoot-JPA

[![Java 17](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.5.4-green.svg)](https://spring.io/projects/spring-boot)
[![Spring Data JPA](https://img.shields.io/badge/Spring%20Data-JPA-blue.svg)](https://spring.io/projects/spring-data-jpa)
[![PostgreSQL](https://img.shields.io/badge/Database-PostgreSQL-teal.svg)](https://www.postgresql.org/)

Aplicación de ejemplo y plantilla base desarrollada con **Java 17** y **Spring Boot 2.5.4**. Implementa una API REST con **Spring MVC**, una arquitectura en capas y persistencia relacional mediante **Spring Data JPA / Hibernate** sobre **PostgreSQL**.

---

## 🚀 Características principales

- **API REST:** controlador Spring MVC para consultar órdenes de compra.
- **Arquitectura en capas:** separación entre controlador, servicio, repositorio y entidad de dominio.
- **Persistencia JPA:** acceso a PostgreSQL mediante Spring Data JPA e Hibernate.
- **Caché:** configuración de Spring Cache con un administrador de caché en memoria.
- **Documentación API:** interfaz Swagger UI proporcionada por SpringFox.
- **Datos de ejemplo:** `import.sql` contiene registros de órdenes para inicializar la aplicación.
- **Calidad y documentación:** configuración de Checkstyle y generación de Javadoc mediante Maven.
- **Contenedorización:** incluye un Dockerfile y un workflow de GitHub Actions para la imagen.

## 🛠️ Stack tecnológico

| Componente | Tecnología | Versión / detalle |
| :--- | :--- | :--- |
| **Lenguaje** | Java | 17 |
| **Framework** | Spring Boot | 2.5.4 |
| **Capa web** | Spring Web / MVC | `spring-boot-starter-web` |
| **Persistencia** | Spring Data JPA / Hibernate | `spring-boot-starter-data-jpa` |
| **Base de datos** | PostgreSQL | JDBC, base de datos `demo` |
| **Exposición de repositorios** | Spring Data REST | `spring-boot-starter-data-rest` |
| **Documentación API** | SpringFox Swagger | 3.0.0 |
| **Caché** | Spring Cache | `ConcurrentMapCacheManager` |
| **Código auxiliar** | Lombok | 1.18.34 |
| **Mapeo** | MapStruct | 1.4.2.Final |
| **Estilo de código** | Maven Checkstyle Plugin | 3.1.2 con Checkstyle 8.42 |
| **Contenedores** | Docker | Imagen basada en OpenJDK 17 |

## 📂 Arquitectura del proyecto

El proyecto organiza el código en capas: el controlador delega las solicitudes al servicio; el servicio contiene las operaciones de negocio y utiliza el repositorio JPA para acceder a las entidades.

```text
Java-SpringBoot-JPA/
├── .agents/skills/               # Guías especializadas para agentes
├── .github/
│   ├── command/                  # Scripts auxiliares
│   └── workflows/                # Workflow para publicar la imagen Docker
├── src/
│   ├── main/
│   │   ├── java/com/example/spring/
│   │   │   ├── config/           # Configuración de caché y Swagger
│   │   │   ├── domain/           # Entidad JPA Order
│   │   │   ├── repository/       # Repositorio Spring Data JPA
│   │   │   ├── service/          # Interfaz e implementación del servicio
│   │   │   ├── web/              # Controladores MVC y REST
│   │   │   └── DemoApplication.java
│   │   └── resources/
│   │       ├── application.yml   # Configuración del servidor y PostgreSQL
│   │       └── import.sql        # Datos iniciales de órdenes
│   └── test/                     # Código de pruebas
├── AGENTS.md                     # Guía del proyecto para agentes y desarrolladores
├── checkstyle.xml                # Reglas de Checkstyle
├── Dockerfile                    # Imagen de la aplicación
└── pom.xml                       # Dependencias y configuración Maven
```

## 📡 Endpoint REST

El controlador de órdenes expone el siguiente endpoint bajo `/api/v1`:

| Método | Ruta | Descripción |
| :--- | :--- | :--- |
| `GET` | `/api/v1/order` | Obtiene la lista de órdenes |

La ruta raíz `/` redirige a `/api/v1/order`. El servicio contiene también operaciones para buscar, guardar, editar y eliminar órdenes; esas operaciones no están expuestas como rutas en el controlador REST actual.

## ⚙️ Inicio rápido

### Requisitos previos

- JDK 17
- Maven 3.6 o superior
- Docker, para ejecutar PostgreSQL

### 1. Iniciar PostgreSQL

La configuración predeterminada utiliza la base de datos `demo`, el usuario `postgres` y la contraseña `postgres`:

```bash
docker run -d -p 5432:5432 \
  --name postgresql \
  -e POSTGRES_DB=demo \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  postgres:14
```

### 2. Revisar el estilo y compilar

```bash
mvn checkstyle:check
mvn clean compile
```

### 3. Ejecutar la aplicación

```bash
mvn spring-boot:run
```

La aplicación inicia en `http://localhost:8080`. Para generar el JAR que utiliza el Dockerfile:

```bash
mvn clean package
```

> **Nota:** `application.yml` configura Hibernate con `ddl-auto: create-drop`. Al iniciar, el esquema se crea y se elimina al cerrar la aplicación; no uses esta configuración para conservar datos en un entorno de producción.

## 📖 Swagger UI

Con la aplicación en ejecución, la documentación interactiva está disponible en:

```text
http://localhost:8080/swagger-ui/
```
