package com.example.spring.web;

import com.example.spring.domain.product.CreateProduct;
import com.example.spring.domain.product.Product;
import com.example.spring.domain.product.UpdateProduct;
import com.example.spring.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Controlador REST para el recurso de productos.
 */
@RestController
@RequestMapping("/api/v1")
public final class ProductController {

    /**
     * Servicio de gestión de productos.
     */
    private final ProductService productService;

    /**
     * Constructor del controlador.
     * @param service Servicio de productos.
     */
    @Autowired
    public ProductController(final ProductService service) {
        this.productService = service;
    }

    /**
     * Crea un nuevo producto.
     * @param product Datos del producto a crear.
     * @return Respuesta con el producto creado o 400 Bad Request.
     */
    @PostMapping("/product")
    public ResponseEntity<Product> save(
            @RequestBody final CreateProduct product) {
        return this.productService.save(product)
                .map(created -> ResponseEntity.status(
                        HttpStatus.CREATED).body(created))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * Actualiza un producto existente.
     * @param product Datos actualizados del producto.
     * @return Respuesta con el producto actualizado o 404 Not Found.
     */
    @PutMapping("/product")
    public ResponseEntity<Product> update(
            @RequestBody final UpdateProduct product) {
        if (product == null) {
            return ResponseEntity.badRequest().build();
        }
        return this.productService.edit(product)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Elimina un producto por su identificador.
     * @param id Identificador del producto.
     * @return 204 No Content si se eliminó o 404 Not Found si no existe.
     */
    @DeleteMapping("/product/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable final UUID id) {
        return this.productService.delete(id)
                .filter(e -> e)
                .map(e -> ResponseEntity.noContent().<Boolean>build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Obtiene todos los productos.
     * @return Lista con los productos disponibles.
     */
    @GetMapping("/product")
    public List<Product> index() {
        return this.productService.findAllProducts();
    }

    /**
     * Obtiene un producto por su identificador.
     * @param id Identificador del producto.
     * @return Respuesta con el producto encontrado o 404 Not Found.
     */
    @GetMapping("/product/{id}")
    public ResponseEntity<Product> findById(@PathVariable final UUID id) {
        return this.productService.findProductById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
