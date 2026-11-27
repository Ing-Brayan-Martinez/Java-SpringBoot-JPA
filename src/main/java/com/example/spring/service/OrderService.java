package com.example.spring.service;

import com.example.spring.domain.order.CreateOrder;
import com.example.spring.domain.order.Order;
import com.example.spring.domain.order.UpdateOrder;
import com.example.spring.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio para operaciones de negocio de órdenes.
 */
@Service
@CacheConfig(cacheNames = {"orderCache"})
public class OrderService {

    /**
     * Repositorio de órdenes.
     */
    private final OrderRepository orderRepository;

    /**
     * Constructor para inyección de dependencias.
     * @param repository Repositorio de órdenes.
     */
    @Autowired
    public OrderService(final OrderRepository repository) {
        this.orderRepository = repository;
    }

    /**
     * Método para persistir una orden.
     * @param order La orden a persistir.
     * @return Optional con la orden guardada o vacío si es inválida.
     */
    public Optional<Order> save(final CreateOrder order) {
        if (order == null
                || order.name() == null
                || order.name().isBlank()
                || order.amount() == null
                || order.amount().compareTo(BigDecimal.ZERO) < 0
                || order.discount() == null
                || order.discount().compareTo(BigDecimal.ZERO) < 0) {
            return Optional.empty();
        }
        final Order entity = new Order();
        entity.setOrderId(UUID.randomUUID());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setName(order.name());
        entity.setAmount(order.amount());
        entity.setDiscount(order.discount());
        return orderRepository.createOrder(entity);
    }

    /**
     * Método para actualizar una orden.
     * @param order La orden a actualizar.
     * @return Optional con la orden actualizada o vacío si es inválida.
     */
    public Optional<Order> edit(final UpdateOrder order) {
        if (order == null
                || order.orderId() == null
                || order.name() == null
                || order.name().isBlank()
                || order.amount() == null
                || order.amount().compareTo(BigDecimal.ZERO) < 0
                || order.discount() == null
                || order.discount().compareTo(BigDecimal.ZERO) < 0) {
            return Optional.empty();
        }
        return orderRepository.findById(order.orderId())
                .flatMap(origin -> {
                    origin.setName(order.name());
                    origin.setAmount(order.amount());
                    origin.setDiscount(order.discount());
                    origin.setUpdatedAt(LocalDateTime.now());
                    return orderRepository.updateOrder(origin);
                });
    }

    /**
     * Método para eliminar una orden según su id.
     * @param id El id de la orden a eliminar.
     * @return Optional con true si existía y se eliminó, o false en otro caso.
     */
    public Optional<Boolean> delete(final UUID id) {
        return orderRepository.existsByOrderId(id)
                .flatMap(exists -> {
                    if (exists) {
                        orderRepository.deleteOrderById(id);
                        return Optional.of(true);
                    }
                    return Optional.of(false);
                });
    }

    /**
     * Método para obtener todas las órdenes.
     * @return Retorna una lista con todas las órdenes.
     */
    @CacheEvict(value = "orderCache", allEntries = true)
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    /**
     * Método para obtener una orden según su id.
     * @param id El id de la orden.
     * @return Optional con la orden encontrada.
     */
    @CachePut(value = "orderCache")
    public Optional<Order> findOrderById(final UUID id) {
        return orderRepository.findOrderById(id);
    }
}
