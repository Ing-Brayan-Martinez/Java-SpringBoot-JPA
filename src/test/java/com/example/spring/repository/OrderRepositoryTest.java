package com.example.spring.repository;

import com.example.spring.domain.order.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas unitarias para el repositorio OrderRepository.
 */
@DataJpaTest
class OrderRepositoryTest {

    /**
     * Repositorio a probar.
     */
    @Autowired
    private OrderRepository orderRepository;

    /**
     * Orden de prueba.
     */
    private Order testOrder;

    /**
     * Inicialización previa a cada prueba.
     */
    @BeforeEach
    void setUp() {
        testOrder = new Order();
        testOrder.setOrderId(UUID.randomUUID());
        testOrder.setName("Orden de prueba");
        testOrder.setAmount(new BigDecimal("150.00"));
        testOrder.setDiscount(new BigDecimal("10.00"));
        testOrder.setCreatedAt(LocalDateTime.now());
        testOrder.setUpdatedAt(LocalDateTime.now());
    }

    /**
     * Prueba los métodos createOrder y findOrderById.
     */
    @Test
    void testCreateAndFindOrderById() {
        final Optional<Order> created = orderRepository.createOrder(testOrder);
        assertTrue(created.isPresent());

        final Optional<Order> found = orderRepository.findOrderById(
                testOrder.getOrderId());
        assertTrue(found.isPresent());
        assertEquals("Orden de prueba", found.get().getName());
    }

    /**
     * Prueba el método findByNameContaining.
     */
    @Test
    void testFindByNameContaining() {
        orderRepository.createOrder(testOrder);

        final List<Order> results = orderRepository.findByNameContaining(
                "prueba");
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
    }

    /**
     * Prueba el método existsByOrderId.
     */
    @Test
    void testExistsByOrderId() {
        orderRepository.createOrder(testOrder);

        final Optional<Boolean> exists = orderRepository.existsByOrderId(
                testOrder.getOrderId());
        assertTrue(exists.isPresent());
        assertTrue(exists.get());

        final Optional<Boolean> notExists = orderRepository.existsByOrderId(
                UUID.randomUUID());
        assertTrue(notExists.isPresent());
        assertFalse(notExists.get());
    }

    /**
     * Prueba el método findByAmountBetween.
     */
    @Test
    void testFindByAmountBetween() {
        orderRepository.createOrder(testOrder);

        final List<Order> inRange = orderRepository.findByAmountBetween(
                new BigDecimal("100.00"), new BigDecimal("200.00"));
        assertEquals(1, inRange.size());

        final List<Order> outOfRange = orderRepository.findByAmountBetween(
                new BigDecimal("300.00"), new BigDecimal("400.00"));
        assertTrue(outOfRange.isEmpty());
    }

    /**
     * Prueba los métodos updateOrder y deleteOrderById.
     */
    @Test
    void testUpdateAndDeleteOrder() {
        orderRepository.createOrder(testOrder);

        testOrder.setName("Orden actualizada");
        final Optional<Order> updated = orderRepository.updateOrder(testOrder);
        assertTrue(updated.isPresent());
        assertEquals("Orden actualizada", updated.get().getName());

        orderRepository.deleteOrderById(testOrder.getOrderId());
        final Optional<Order> found = orderRepository.findOrderById(
                testOrder.getOrderId());
        assertFalse(found.isPresent());
    }
}
