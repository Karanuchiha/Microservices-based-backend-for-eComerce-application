package com.karan.saleservice.repositories;

import com.karan.saleservice.model.Order;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
    List<Order> findByUserId(String userId);
}
