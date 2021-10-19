package com.example.spring.service;

import com.example.spring.domain.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    public List<Order> findAllOrders();

    public Optional<Order> findOrderById(Long id);

    public void save(Order order);

    public void edit(Order order);

    public void delete(Long id);


}
