package com.cs.liveorderboard.service;

import com.cs.liveorderboard.domain.*;
import com.cs.liveorderboard.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

import static com.cs.liveorderboard.domain.OrderType.*;
import static com.cs.liveorderboard.domain.Unit.*;

@Service
public class OrderService {

    @Autowired
    OrderRepository repository;

    public Order createOrder(Order order) {
        return repository.insert(order);
    }

    public void deleteOrderById(String id) {
        repository.deleteById(id);
    }

    public List<OrderResponse> getAllOrders() {
        return transformOrders();
    }

    private List<OrderResponse> transformOrders() {
        List<Order> orders = repository.findAll();

        Map<@NotNull OrderType, List<Order>> orderByOrderType = orders.stream().collect(Collectors.groupingBy(Order::getOrderType));

        List<OrderDetail> buyOrderDetails = orderByOrderType.getOrDefault(BUY, Collections.emptyList())
                .stream()
                .map(Order::toOrderDetails)
                .collect(Collectors.toList());

        List<OrderDetail> groupBuyOrderByPrice = groupByPrice(buyOrderDetails);
        groupBuyOrderByPrice.sort(Comparator.comparing(OrderDetail::getPriceAmount).reversed());

        List<OrderDetail> sellOrderDetails = orderByOrderType.getOrDefault(SELL, Collections.emptyList())
                .stream()
                .map(Order::toOrderDetails)
                .collect(Collectors.toList());


        List<OrderDetail> groupSellOrderByPrice = groupByPrice(sellOrderDetails);

        groupSellOrderByPrice.sort(Comparator.comparing(order -> order.getPrice().getAmount()));


        if (!sellOrderDetails.isEmpty() && !buyOrderDetails.isEmpty()) {
            return Arrays.asList(OrderResponse.of(SELL, groupSellOrderByPrice), OrderResponse.of(BUY, groupBuyOrderByPrice));
        } else if (sellOrderDetails.isEmpty()) {
            return Arrays.asList(OrderResponse.of(BUY, groupBuyOrderByPrice));
        } else {
            return Arrays.asList(OrderResponse.of(SELL, groupSellOrderByPrice));
        }
    }

    private List<OrderDetail> groupByPrice(List<OrderDetail> orderResponses) {
        List<OrderDetail> orderDetails = orderResponses.stream().collect(
                Collectors.groupingBy(OrderDetail::getPrice))
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> merge(entry.getValue())))
                .values().stream().flatMap(Collection::stream).collect(Collectors.toList());

        return orderDetails;
    }

    private List<OrderDetail> merge(List<OrderDetail> orderDetails) {
        double totalQuantity = orderDetails.stream()
                .mapToDouble(detail -> detail.getQuantity().getValue()).sum();

        OrderDetail orderDetail = orderDetails.stream().findFirst().get();
        orderDetail.setQuantity(new Quantity(totalQuantity, kg));
        return Collections.singletonList(orderDetail);
    }


}
