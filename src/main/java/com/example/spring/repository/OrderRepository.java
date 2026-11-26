package com.example.spring.repository;

import com.example.spring.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio JPA para la entidad Order.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    /**
     * Consulta JPQL para buscar una orden por su identificador.
     * @param id Identificador de la orden.
     * @return Optional con la orden encontrada.
     */
    @Query("SELECT o FROM Order o WHERE o.orderId = :id")
    Optional<Order> findOrderById(@Param("id") final UUID id);

    /**
     * Consulta JPQL para buscar órdenes por coincidencia en el nombre.
     * @param name Nombre o fragmento a buscar.
     * @return Lista de órdenes encontradas.
     */
    @Query("SELECT o FROM Order o WHERE LOWER(o.name) LIKE "
            + "LOWER(CONCAT('%', :name, '%'))")
    List<Order> findByNameContaining(@Param("name") final String name);

    /**
     * Consulta JPQL para verificar si existe una orden por ID.
     * @param id Identificador de la orden.
     * @return true si existe, false en caso contrario.
     */
    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END "
            + "FROM Order o WHERE o.orderId = :id")
    boolean existsByOrderId(@Param("id") final UUID id);

    /**
     * Consulta JPQL modificadora para actualizar una orden.
     * @param id Identificador de la orden.
     * @param name Nuevo nombre.
     * @param amount Nuevo monto.
     * @param discount Nuevo descuento.
     * @param updatedAt Fecha de modificación.
     * @return Cantidad de filas afectadas.
     */
    @Modifying
    @Query("UPDATE Order o SET o.name = :name, o.amount = :amount, "
            + "o.discount = :discount, o.updatedAt = :updatedAt "
            + "WHERE o.orderId = :id")
    int updateOrder(
            @Param("id") final UUID id,
            @Param("name") final String name,
            @Param("amount") final BigDecimal amount,
            @Param("discount") final BigDecimal discount,
            @Param("updatedAt") final LocalDateTime updatedAt);

    /**
     * Consulta JPQL para filtrar órdenes por rango de montos.
     * @param minAmount Monto mínimo.
     * @param maxAmount Monto máximo.
     * @return Lista de órdenes que cumplen la condición.
     */
    @Query("SELECT o FROM Order o WHERE o.amount BETWEEN "
            + ":minAmount AND :maxAmount")
    List<Order> findByAmountBetween(
            @Param("minAmount") final BigDecimal minAmount,
            @Param("maxAmount") final BigDecimal maxAmount);
}
