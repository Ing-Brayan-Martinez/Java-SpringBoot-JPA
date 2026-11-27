package com.example.spring.web;

import com.example.spring.domain.order.CreateOrder;
import com.example.spring.domain.order.Order;
import com.example.spring.domain.order.UpdateOrder;
import com.example.spring.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Controlador REST para el recurso de órdenes.
 */
@RestController
@RequestMapping("/api/v1")
public final class OrderController {

    /**
     * Servicio de gestión de órdenes.
     */
    private final OrderService orderService;

    /**
     * Constructor del controlador.
     * @param service Servicio de órdenes.
     */
    @Autowired
    public OrderController(final OrderService service) {
        this.orderService = service;
    }

    /**
     * Crea una nueva orden.
     * @param order Datos de la orden a crear.
     * @return Respuesta con la orden creada o 400 Bad Request.
     */
    @PostMapping("/order")
    public ResponseEntity<Order> save(@RequestBody final CreateOrder order) {
        return this.orderService.save(order)
                .map(created -> ResponseEntity.status(
                        HttpStatus.CREATED).body(created))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * Actualiza una orden existente.
     * @param order Datos actualizados de la orden.
     * @return Respuesta con la orden actualizada o 404 Not Found.
     */
    @PutMapping("/order")
    public ResponseEntity<Order> update(@RequestBody final UpdateOrder order) {
        if (order == null) {
            return ResponseEntity.badRequest().build();
        }
        return this.orderService.edit(order)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Elimina una orden por su identificador.
     * @param id Identificador de la orden.
     * @return 204 No Content si se eliminó o 404 Not Found si no existe.
     */
    @DeleteMapping("/order/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable final UUID id) {
        return this.orderService.delete(id)
            .filter((e) -> e == true)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Obtiene todas las órdenes.
     * @return Lista con las órdenes disponibles.
     */
    @GetMapping("/order")
    public List<Order> index() {
        return this.orderService.findAllOrders();
    }

    /**
     * Obtiene una orden por su identificador.
     * @param id Identificador de la orden.
     * @return Respuesta con la orden encontrada o 404 Not Found.
     */
    @GetMapping("/order/{id}")
    public ResponseEntity<Order> findById(@PathVariable final UUID id) {
        return this.orderService.findOrderById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
