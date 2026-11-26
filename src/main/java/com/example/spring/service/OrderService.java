package com.example.spring.service.impl;

import com.example.spring.domain.order.Order;
import com.example.spring.domain.order.CreateOrder;
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

@Service
@CacheConfig(cacheNames = {"orderCache"})
public class OrderService {

    /**
     * Variable que almacena el repositorio de ordenes.
     */
    private OrderRepository orderRepository;

    /**
     * Consructor de esta clase.
     * @param orderRepository Repositorio de ordenes.
     */
    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Método para persistir una orden.
     * @param order La order para pesistir
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
        return Optional.ofNullable(order)
        .map((dto) -> {
            final Order entity = new Order();
            entity.setOrderId(UUID.randomUUID());
            entity.setCreatedAt(LocalDateTime.now());
            entity.setUpdatedAt(LocalDateTime.now());
            entity.setName(dto.name());
            entity.setAmount(dto.amount());
            entity.setDiscount(dto.discount());
            return entity;
        })
        .flatMap(orderRepository::save);
    }

    /**
     * Método para actualizar una orden.
     * @param order La order a actualizar
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
        .map((origin) -> {
            origin.setName(order.name());
            origin.setAmount(order.amount());
            origin.setDiscount(order.discount());
            origin.setUpdatedAt(LocalDateTime.now());
            return origin;
        })
        .flatMap(orderRepository::edit);
    }

    /**
     * Método para eliminar una orden según su id.
     * @param id El id de la orden a eliminar
     */
    public Optional<Boolean> delete(UUID id) {
        return orderRepository.exstsById(id)
        .flatMap((exists) -> exists
            ? Optional.of(id).map((e) -> orderRepository.deleteById(e)).map((e) -> true)
            : Optional.of(false));
    }

    /**
     * Método para obtener todas las órdenes.
     * @return Retorna una lista órdenes
     */
    @CacheEvict(value = "orderCache", allEntries = true)
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    /**
     * Método para obtener una orden segun su id.
     * @param id El id de la orden
     * @return Retorna la orden encontrada
     */
    @CachePut(value = "orderCache")
    public Optional<Order> findOrderById(UUID id) {
        return orderRepository.findById(id);
    }
}


