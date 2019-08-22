package com.cs.liveorderboard.controller;

import com.cs.liveorderboard.domain.Order;
import com.cs.liveorderboard.domain.OrderResponse;
import com.cs.liveorderboard.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin(allowedHeaders = {"GET", "POST", "DELETE"})
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<Order> create(@Valid @RequestBody Order order) {

        Order newOrder = orderService.createOrder(order);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newOrder.getId())
                .toUri();

        return ResponseEntity.created(location).body(newOrder);
    }

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        orderService.deleteOrderById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/orders")
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrders();
    }
}
