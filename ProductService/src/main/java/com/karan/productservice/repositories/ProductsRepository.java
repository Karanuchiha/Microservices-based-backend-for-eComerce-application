package com.karan.productservice.repositories;

import com.karan.productservice.model.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsRepository extends ReactiveMongoRepository<Product, String> {

}
