package com.example.spring.service;

import com.example.spring.domain.product.CreateProduct;
import com.example.spring.domain.product.Product;
import com.example.spring.domain.product.UpdateProduct;
import com.example.spring.repository.ProductRepository;
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

/**
 * Servicio para operaciones de negocio de productos.
 */
@Service
@CacheConfig(cacheNames = {"productCache"})
public class ProductService {

    /**
     * Repositorio de productos.
     */
    private final ProductRepository productRepository;

    /**
     * Constructor para inyección de dependencias.
     * @param repository Repositorio de productos.
     */
    @Autowired
    public ProductService(final ProductRepository repository) {
        this.productRepository = repository;
    }

    /**
     * Método para persistir un producto.
     * @param product El producto a persistir.
     * @return Optional con el producto guardado o vacío si es inválido.
     */
    public Optional<Product> save(final CreateProduct product) {
        if (product == null
                || product.name() == null
                || product.name().isBlank()
                || product.price() == null
                || product.price().compareTo(BigDecimal.ZERO) < 0
                || product.stock() == null
                || product.stock() < 0) {
            return Optional.empty();
        }
        final Product entity = new Product();
        entity.setProductId(UUID.randomUUID());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setName(product.name());
        entity.setPrice(product.price());
        entity.setStock(product.stock());
        return productRepository.createProduct(entity);
    }

    /**
     * Método para actualizar un producto.
     * @param product El producto a actualizar.
     * @return Optional con el producto actualizado o vacío si es inválido.
     */
    public Optional<Product> edit(final UpdateProduct product) {
        if (product == null
                || product.productId() == null
                || product.name() == null
                || product.name().isBlank()
                || product.price() == null
                || product.price().compareTo(BigDecimal.ZERO) < 0
                || product.stock() == null
                || product.stock() < 0) {
            return Optional.empty();
        }
        return productRepository.findById(product.productId())
                .flatMap(origin -> {
                    origin.setName(product.name());
                    origin.setPrice(product.price());
                    origin.setStock(product.stock());
                    origin.setUpdatedAt(LocalDateTime.now());
                    return productRepository.updateProduct(origin);
                });
    }

    /**
     * Método para eliminar un producto según su id.
     * @param id El id del producto a eliminar.
     * @return Optional con true si existía y se eliminó, o false en otro caso.
     */
    public Optional<Boolean> delete(final UUID id) {
        return productRepository.existsByProductId(id)
                .flatMap(exists -> {
                    if (exists) {
                        productRepository.deleteProductById(id);
                        return Optional.of(true);
                    }
                    return Optional.of(false);
                });
    }

    /**
     * Método para obtener todos los productos.
     * @return Retorna una lista con todos los productos.
     */
    @CacheEvict(value = "productCache", allEntries = true)
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Método para obtener un producto según su id.
     * @param id El id del producto.
     * @return Optional con el producto encontrado.
     */
    @CachePut(value = "productCache")
    public Optional<Product> findProductById(final UUID id) {
        return productRepository.findProductById(id);
    }
}
