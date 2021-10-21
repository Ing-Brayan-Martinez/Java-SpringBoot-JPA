package com.example.spring.service;

import com.example.spring.domain.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    /**
     * Método para obtener todas las órdenes.
     * @return Retorna una lista órdenes
     */
    List<Order> findAllOrders();

    /**
     * Método para obtener una orden segun su id.
     * @param id El id de la orden
     * @return Retorna la orden encontrada
     */
    Optional<Order> findOrderById(Long id);

    /**
     * Método para persistir una orden.
     * @param order La order para pesistir
     */
    void save(Order order);

    /**
     * Método para actualizar una orden.
     * @param order La order a actualizar
     */
    void edit(Order order);

    /**
     * Método para eliminar una orden según su id.
     * @param id El id de la orden a eliminar
     */
    void delete(Long id);


}
