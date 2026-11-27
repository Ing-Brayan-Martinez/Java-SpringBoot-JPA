package com.example.spring.repository;

import com.example.spring.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio JPA para la entidad Order.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    /**
     * Persiste una entidad Order en la base de datos.
     * @param order Entidad Order a guardar.
     * @return Optional con la entidad guardada.
     */
    default Optional<Order> createOrder(Order order) {
        return Optional.of(save(order));
    }

    /**
     * Actualiza una entidad Order existente.
     * @param order Entidad Order a actualizar.
     * @return Optional con la entidad actualizada.
     */
    default Optional<Order> updateOrder(Order order) {
        return Optional.of(save(order));
    }

    /**
     * Elimina una orden por su ID mediante consulta JPQL.
     * @param id Identificador de la orden a eliminar.
     */
    @Modifying
    @Query("DELETE FROM Order o WHERE o.orderId = :id")
    void deleteByOrderId(@Param("id") UUID id);

    /**
     * Elimina una orden según su ID y retorna un Optional vacío.
     * @param id Identificador de la orden a eliminar.
     * @return Optional con Void.
     */
    default Optional<Void> deleteOrderById(UUID id) {
        deleteByOrderId(id);
        return Optional.empty();
    }

    /**
     * Consulta JPQL para buscar una orden por su identificador.
     * @param id Identificador de la orden.
     * @return Optional con la orden encontrada.
     */
    @Query("SELECT o FROM Order o WHERE o.orderId = :id")
    Optional<Order> findOrderById(@Param("id") UUID id);

    /**
     * Consulta JPQL para buscar órdenes por coincidencia en el nombre.
     * @param name Nombre o fragmento a buscar.
     * @return Lista de órdenes encontradas.
     */
    @Query("SELECT o FROM Order o WHERE LOWER(o.name) LIKE "
            + "LOWER(CONCAT('%', :name, '%'))")
    List<Order> findByNameContaining(@Param("name") String name);

    /**
     * Consulta JPQL para verificar si existe una orden por ID.
     * @param id Identificador de la orden.
     * @return Optional con verdadero si existe la orden.
     */
    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END "
            + "FROM Order o WHERE o.orderId = :id")
    Optional<Boolean> existsByOrderId(@Param("id") UUID id);

    /**
     * Consulta JPQL para filtrar órdenes por rango de montos.
     * @param minAmount Monto mínimo.
     * @param maxAmount Monto máximo.
     * @return Lista de órdenes que cumplen la condición.
     */
    @Query("SELECT o FROM Order o WHERE o.amount BETWEEN "
            + ":minAmount AND :maxAmount")
    List<Order> findByAmountBetween(
            @Param("minAmount") BigDecimal minAmount,
            @Param("maxAmount") BigDecimal maxAmount);
}
