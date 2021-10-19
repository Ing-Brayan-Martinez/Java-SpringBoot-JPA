package com.example.spring.web;

import com.example.spring.domain.Order;
import com.example.spring.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public final class OrderController {

    @Resource
    private OrderService orderService;

    @GetMapping("/order")
    public List<Order> index() {
        return this.orderService.findAllOrders();
    }

}
