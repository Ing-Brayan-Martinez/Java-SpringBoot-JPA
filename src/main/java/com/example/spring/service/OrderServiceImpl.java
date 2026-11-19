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

@Service
@CacheConfig(cacheNames = {"orderCache"})
public class OrderServiceImpl implements OrderService {

    /**
     * Variable que almacena el repositorio de ordenes.
     */
    private OrderRepository orderRepository;

    /**
     * Consructor de esta clase.
     * @param orderRepository Repositorio de ordenes.
     */
    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Método para obtener todas las órdenes.
     * @return Retorna una lista órdenes
     */
    @Override
    @CacheEvict(value = "orderCache", allEntries = true)
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    /**
     * Método para obtener una orden segun su id.
     * @param id El id de la orden
     * @return Retorna la orden encontrada
     */
    @Override
    @CachePut(value = "orderCache")
    public Optional<Order> findOrderById(Long id) {
        return orderRepository.findById(id);
    }

    /**
     * Método para persistir una orden.
     * @param order La order para pesistir
     */
    @Override
    public void save(Order order) {
        orderRepository.save(order);
    }

    /**
     * Método para actualizar una orden.
     * @param order La order a actualizar
     */
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

    /**
     * Método para eliminar una orden según su id.
     * @param id El id de la orden a eliminar
     */
    @Override
    public void delete(Long id) {
        orderRepository.deleteById(id);
    }
}


