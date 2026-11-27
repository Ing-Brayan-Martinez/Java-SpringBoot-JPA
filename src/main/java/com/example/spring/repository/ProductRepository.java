package com.example.spring.repository;

import com.example.spring.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio JPA para la entidad Product.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    /**
     * Persiste una entidad Product en la base de datos.
     * @param product Entidad Product a guardar.
     * @return Optional con la entidad guardada.
     */
    default Optional<Product> createProduct(Product product) {
        return Optional.of(save(product));
    }

    /**
     * Actualiza una entidad Product existente.
     * @param product Entidad Product a actualizar.
     * @return Optional con la entidad actualizada.
     */
    default Optional<Product> updateProduct(Product product) {
        return Optional.of(save(product));
    }

    /**
     * Elimina un producto por su ID mediante consulta JPQL.
     * @param id Identificador del producto a eliminar.
     */
    @Modifying
    @Query("DELETE FROM Product p WHERE p.productId = :id")
    void deleteByProductId(@Param("id") UUID id);

    /**
     * Elimina un producto según su ID y retorna un Optional vacío.
     * @param id Identificador del producto a eliminar.
     * @return Optional con Void.
     */
    default Optional<Void> deleteProductById(UUID id) {
        deleteByProductId(id);
        return Optional.empty();
    }

    /**
     * Consulta JPQL para buscar un producto por su identificador.
     * @param id Identificador del producto.
     * @return Optional con el producto encontrado.
     */
    @Query("SELECT p FROM Product p WHERE p.productId = :id")
    Optional<Product> findProductById(@Param("id") UUID id);

    /**
     * Consulta JPQL para verificar si existe un producto por ID.
     * @param id Identificador del producto.
     * @return Optional con verdadero si existe el producto.
     */
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END "
            + "FROM Product p WHERE p.productId = :id")
    Optional<Boolean> existsByProductId(@Param("id") UUID id);
}
