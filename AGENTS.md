# Guía de Agentes y Desarrolladores — Java-SpringBoot-JPA

Este documento define el stack tecnológico, la arquitectura basada en Spring Data JPA / REST, los patrones de diseño y las reglas críticas que **TODOS** los agentes de inteligencia artificial y desarrolladores deben seguir obligatoriamente al trabajar en este proyecto.

---

## 1. Stack Tecnológico

El proyecto está diseñado bajo una **Arquitectura en Capas Tradicional con Spring Boot y JPA (Spring Data JPA / Hibernate)**.

---

## Project Skills & Knowledge Base

Este proyecto utiliza un sistema modular de "Skills" ubicado en [.agents/skills/](.agents/skills/). Estos documentos contienen guías especializadas y patrones para tareas específicas del proyecto.

**El agente DEBE seguir este protocolo:**

1. **Discovery**: Consultar `.agents/skills/` para identificar documentación relevante.
2. **Activation**: Leer el archivo `SKILL.md` de las skills relevantes:
   - [Contenedorización con Docker](.agents/skills/spring-implements-docker/SKILL.md) (`spring-implements-docker`)
   - [Configuración de Maven](.agents/skills/spring-implements-maven/SKILL.md) (`spring-implements-maven`)
3. **Compliance**: Aplicar los patrones definidos en la skill correspondiente.

| Categórica | Tecnología / Librería | Versión / Detalle |
| :--- | :--- | :--- |
| **Lenguaje & Runtime** | Java 17 | JDK 17 en compilación y ejecución |
| **Framework Base** | Spring Boot | `2.5.4` (`spring-boot-starter-parent`) |
| **Capa Web** | Spring Web / MVC | `spring-boot-starter-web` (Controladores REST y MVC) |
| **Documentación API** | SpringFox Swagger | `3.0.0` (`springfox-boot-starter`, `springfox-swagger-ui`) |
| **Acceso a Datos** | Spring Data JPA & REST | `spring-boot-starter-data-jpa`, `spring-boot-starter-data-rest` |
| **Driver BD** | PostgreSQL Driver | `org.postgresql:postgresql` |
| **Base de Datos** | PostgreSQL | Acceso mediante URL JDBC (`jdbc:postgresql://localhost:5432/demo`) |
| **Caché** | Spring Cache | `@EnableCaching`, `ConcurrentMapCacheManager` |
| **Mapeo y Boilerplate** | Lombok & MapStruct | Lombok `1.18.20` y MapStruct `1.4.2.Final` |
| **Calidad de Código** | Checkstyle | `maven-checkstyle-plugin` `3.1.2` con Checkstyle `8.42` (`checkstyle.xml`) |
| **Testing** | JUnit 5 / Spring Boot Test | `spring-boot-starter-test` |
| **Despliegue & CI/CD** | Docker & GitHub Actions | `Dockerfile` (openjdk:17) y GitHub Actions (`docker-publish.yml`) |

---

## 2. Reglas Críticas e Inviolables (MANDATORY)

1. **Inyección de Dependencias por Constructor:**
   - Todas las dependencias en `@Service`, `@RestController` o `@Configuration` deben inyectarse a través del constructor anotado con `@Autowired`.
   - **EVITAR** `@Autowired` directo sobre campos de clase (`field injection`).

2. **Mapeo Estricto de Entidades JPA:**
   - Usar anotaciones JPA estándar (`@Entity`, `@Table`, `@Id`, `@GeneratedValue`, `@Column`).
   - Mantener adecuadamente los callbacks de ciclo de vida JPA (`@PrePersist`, `@PreUpdate`, `@PreRemove`) cuando sea necesario.

3. **Cumplimiento Estricto de Checkstyle y Javadoc:**
   - Todo cambio de código debe pasar la validación `mvn checkstyle:check` sin errores ni advertencias.
   - **TODAS** las clases, interfaces, métodos (públicos y protegidos) y campos privados deben tener sus respectivos comentarios Javadoc.
   - Todos los paquetes de código dentro de `src/main/java/` deben contener un archivo `package-info.java` documentado.

4. **Inviolabilidad de Arquitectura en Capas:**
   - **Controlador (`web`)** $\rightarrow$ delega únicamente al **Servicio (`service`)**.
   - **Servicio (`service`)** $\rightarrow$ implementa la lógica de negocio, cacheo y consulta al **Repositorio (`repository`)**.
   - **Repositorio (`repository`)** $\rightarrow$ hereda de `JpaRepository<T, ID>` y mapea entidades de **Dominio (`domain`)**.
   - No saltearse capas ni colocar lógica de persistencia en los controladores.

5. **Verificación Obligatoria de Calidad:**
   - Antes de dar por completada cualquier tarea, se deben ejecutar los comandos de compilación, linter y tests.

---

## 3. Estructura del Proyecto

```text
Java-SpringBoot-JPA/
├── .github/
│   ├── command/                   # Scripts auxiliares (command.sh)
│   └── workflows/                 # CI/CD pipelines (docker-publish.yml)
├── src/
│   ├── main/
│   │   ├── java/com/example/spring/
│   │   │   ├── config/            # Configuraciones (CachingConfig, SpringFoxConfig)
│   │   │   │   └── package-info.java
│   │   │   ├── domain/            # Entidades JPA (@Entity, @Table)
│   │   │   │   └── package-info.java
│   │   │   ├── repository/        # Repositorios JPA (JpaRepository)
│   │   │   │   └── package-info.java
│   │   │   ├── service/           # Interfaces de servicios
│   │   │   │   ├── impl/          # Implementaciones de servicios (@Service)
│   │   │   │   │   └── package-info.java
│   │   │   │   └── package-info.java
│   │   │   ├── web/               # Controladores REST (@RestController)
│   │   │   │   └── package-info.java
│   │   │   ├── DemoApplication.java # Clase principal de arranque
│   │   │   └── package-info.java
│   │   └── resources/
│   │       ├── application.yml    # Propiedades del servidor y datasource PostgreSQL
│   │       └── import.sql         # Inserción DML de datos iniciales
│   └── test/
│       └── java/com/example/      # Tests unitarios y de integración
├── checkstyle.xml                 # Reglas estrictas de Checkstyle Sun/Google
├── Dockerfile                     # Construcción de imagen de contenedor
├── pom.xml                        # Configuración de dependencias y plugins Maven
└── AGENTS.md                      # Instrucciones y guía para agentes de IA
```

---

## 4. Patrones de Implementación (Código de Referencia)

### A. Entidad de Dominio (`domain`)
Las entidades de dominio deben usar anotaciones JPA (`@Entity`, `@Table`, `@Id`, `@GeneratedValue`, `@Column`) y Lombok (`@Getter`, `@Setter`, `@ToString`, `@RequiredArgsConstructor`).

```java
package com.example.spring.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Representa la entidad de dominio para las órdenes de compra.
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "order", schema = "public")
public final class Order {

    /**
     * Identificador único de la orden.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    /**
     * Nombre o descripción de la orden.
     */
    @Column
    private String name;

    /**
     * Monto total de la orden.
     */
    @Column
    private BigDecimal amount;

    /**
     * Descuento aplicado.
     */
    @Column
    private BigDecimal discount;

    /**
     * Fecha y hora de creación.
     */
    @Column
    private Date createdAt;

    /**
     * Fecha y hora de última actualización.
     */
    @Column
    private Date updatedAt;
}
```

---

### B. Repositorio (`repository`)
Heredar de `JpaRepository<Entity, ID>`.

```java
package com.example.spring.repository;

import com.example.spring.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad Order.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
```

---

### C. Capa de Servicio (`service` y `service.impl`)
Definir interfaz y su correspondiente implementación anotada con `@Service`, `@CacheConfig` e inyección por constructor.

#### Interfaz
```java
package com.example.spring.service;

import com.example.spring.domain.Order;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para operaciones de negocio de órdenes.
 */
public interface OrderService {

    /**
     * Obtiene todas las órdenes existentes.
     * @return Lista con todas las órdenes encontradas.
     */
    List<Order> findAllOrders();

    /**
     * Obtiene una orden por su ID.
     * @param id Identificador de la orden.
     * @return Optional con la orden encontrada o vacío si no existe.
     */
    Optional<Order> findOrderById(Long id);

    /**
     * Persiste una nueva orden.
     * @param order Entidad orden a guardar.
     */
    void save(Order order);

    /**
     * Edita una orden existente.
     * @param order Entidad orden con los cambios.
     */
    void edit(Order order);

    /**
     * Elimina una orden según su ID.
     * @param id Identificador de la orden a eliminar.
     */
    void delete(Long id);
}
```

#### Implementación
```java
package com.example.spring.service.impl;

import com.example.spring.domain.Order;
import com.example.spring.repository.OrderRepository;
import com.example.spring.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de órdenes.
 */
@Service
@CacheConfig(cacheNames = {"orderCache"})
public class OrderServiceImpl implements OrderService {

    /**
     * Repositorio de órdenes.
     */
    private OrderRepository orderRepository;

    /**
     * Constructor para inyección de dependencias.
     * @param orderRepository Repositorio de órdenes.
     */
    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    @CacheEvict(value = "orderCache", allEntries = true)
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    @CachePut(value = "orderCache")
    public Optional<Order> findOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public void save(Order order) {
        orderRepository.save(order);
    }

    @Override
    public void edit(Order order) {
        final Optional<Order> result = orderRepository.findById(order.getOrderId());
        result.ifPresent(origin -> {
            origin.setName(order.getName());
            origin.setAmount(order.getAmount());
            origin.setDiscount(order.getDiscount());
            orderRepository.save(origin);
        });
    }

    @Override
    public void delete(Long id) {
        orderRepository.deleteById(id);
    }
}
```

---

### D. Controlador REST (`web`)
Usar `@RestController` y `@RequestMapping`. Inyectar dependencias por constructor.

```java
package com.example.spring.web;

import com.example.spring.domain.Order;
import com.example.spring.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador REST para el recurso de órdenes.
 */
@RestController
@RequestMapping("/api/v1")
public final class OrderController {

    /**
     * Servicio de gestión de órdenes.
     */
    private OrderService orderService;

    /**
     * Constructor para inyección de dependencias.
     * @param orderService Servicio de órdenes.
     */
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Endpoint GET que expone el listado completo de órdenes.
     * @return Lista con el conjunto de órdenes disponibles.
     */
    @GetMapping("/order")
    public List<Order> index() {
        return this.orderService.findAllOrders();
    }
}
```

---

### E. Archivos `package-info.java`
Cada paquete Java en `src/main/java/` **DEBE** contar con un archivo `package-info.java` para cumplir con Checkstyle (`JavadocPackage`).

```java
/**
 * Paquete de controladores REST de la aplicación.
 */
package com.example.spring.web;
```

---

## 5. Pruebas y Validación

Para probar los controladores y servicios en Spring Boot:

```java
@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testFindAllOrders() throws Exception {
        mockMvc.perform(get("/api/v1/order"))
               .andExpect(status().isOk());
    }
}
```

---

## 6. Workflow de Verificación (Pre-completion Verification)

Antes de reportar cualquier tarea como completada, el agente **DEBE** ejecutar y validar:

1. **Verificación de Estilo y Javadoc (Checkstyle):**
   ```bash
   mvn checkstyle:check
   ```
   *Debe resultar sin errores de formato, faltas de Javadoc ni violaciones de reglas.*

2. **Compilación y Pruebas Unitarias:**
   ```bash
   mvn test
   ```
   *Todos los tests deben pasar exitosamente.*

3. **Compilación Limpia y Empaquetado:**
   ```bash
   mvn clean package -DskipTests
   ```
   *Garantiza que el artefacto JAR se genere correctamente.*
