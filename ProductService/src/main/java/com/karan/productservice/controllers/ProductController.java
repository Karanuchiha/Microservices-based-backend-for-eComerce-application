package com.karan.productservice.controllers;

import com.karan.productservice.model.InventoryUpdate;
import com.karan.productservice.model.Product;
import com.karan.productservice.repositories.ProductsRepository;
import com.karan.productservice.utility.ProductUtils;
import java.util.Arrays;

import javax.annotation.PreDestroy;
import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
@Slf4j
public class ProductController {

    private static final String PRODUCT_WITH_ID = "Product with id";

    @Autowired
    private ProductsRepository repository;

    @CrossOrigin
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Product>> searchProductById(@PathVariable String id) {
        return repository.findById(id)
                .map(product -> ResponseEntity.status(HttpStatus.OK).body(product))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @CrossOrigin
    @GetMapping("/price/{id}")
    public Mono<ResponseEntity<Double>> getProductPrice(@PathVariable String id) {

        return repository.findById(id)
                .map(product -> ResponseEntity.status(HttpStatus.OK).body(product.getPrice()))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @CrossOrigin
    @GetMapping(params = {"searchString"})
    public Mono<ResponseEntity<List<Product>>> searchProductsNameOrDescription(
            @RequestParam String searchString) {
        Mono<List<Product>> allProduct = repository.findAll(ProductUtils.searchRepositoryByExample(searchString)).collectList();
        return allProduct.map(products -> ResponseEntity.status(HttpStatus.OK).body(products))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @CrossOrigin
    @GetMapping()
    public Mono<ResponseEntity<List<Product>>> getAllProducts() {
        Mono<List<Product>> allProduct = repository.findAll().collectList();
        return allProduct.map(products -> ResponseEntity.status(HttpStatus.OK).body(products))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @CrossOrigin
    @PostMapping()
    @PreAuthorize("hasAuthority('Admin')")
    public Mono<ResponseEntity<Product>> newProduct(@Valid @RequestBody Product newProduct) {
        log.info(
                String.format(
                        "%s %s and name %s has been created",
                        PRODUCT_WITH_ID, newProduct.getId(), newProduct.getName()));

        return repository.save(newProduct).map(product -> ResponseEntity.status(HttpStatus.OK).body(product));
    }

    @CrossOrigin
    @PutMapping()
    @PreAuthorize("hasAuthority('Admin')")
    public Mono<ResponseEntity> updateProduct(@Valid @RequestBody Product updatedProduct) {
        Optional<Product> optionalProduct = repository.findById(updatedProduct.getId()).blockOptional();
        if (optionalProduct.isPresent()) {
            Product productToUpdate = optionalProduct.get();
            if (ProductUtils.canProductBeUpdated(updatedProduct, productToUpdate)) {
                productToUpdate.updateProduct(updatedProduct);
                log.info(
                        String.format(" %s %s has been updated ", PRODUCT_WITH_ID, productToUpdate.getId()));
                return repository.save(productToUpdate).map(product
                        -> ResponseEntity.ok(productToUpdate));
            } else {
                return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(String.format(
                                "The product cannot be updated")));
            }
        }
        return Mono.just(ResponseEntity.badRequest().build());
    }

    @CrossOrigin
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<Boolean> updateStock(
            @PathVariable Product productToUpdate, @RequestBody InventoryUpdate inventoryUpdate) {
        boolean updated;
        ResponseEntity<Boolean> response;
        updated = ProductUtils.updateOrRemoveStock(inventoryUpdate, productToUpdate);
        repository.save(productToUpdate);
        if (updated) {
            log.info(
                    String.format(
                            " %s %s has been updated new stock level is : %s",
                            PRODUCT_WITH_ID, productToUpdate.getId(), productToUpdate.getStockLevel()));
            response = ResponseEntity.ok(true);
        } else {
            log.info(
                    String.format(
                            " %s %s has not been updated stock level is : %s",
                            PRODUCT_WITH_ID, productToUpdate.getId(), productToUpdate.getStockLevel()));
            response = ResponseEntity.badRequest().build();
        }

        return response;
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('Admin')")
    public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable String id) {
        return repository.findById(id).flatMap(product -> repository.delete(product).then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @PreDestroy
    public void insertProductObject() {
        log.info("test object inserted");
        newProduct(new Product("can be used to eat", "spoon ", 12, 2.9, "imageURL", Arrays.asList("cat")));
    }
}
