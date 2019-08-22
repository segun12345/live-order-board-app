package com.cs.liveorderboard.repository;

import com.cs.liveorderboard.domain.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    Order insert(Order order);

    void deleteById(String id);

    List<Order> findAll();
}
