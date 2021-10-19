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
@CacheConfig(cacheNames={"orderCache"})
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    @CacheEvict(value="orderCache", allEntries=true)
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    @CachePut(value="orderCache")
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


