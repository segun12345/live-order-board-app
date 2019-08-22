package com.cs.liveorderboard.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor(staticName = "of")
public class Order {
    @Id private String id;

    @NotNull
    private String userId;

    @NotNull
    private Quantity quantity;

    @NotNull
    private Price price;

    @NotNull
    private OrderType orderType;

    public Order() {}

    public OrderDetail toOrderDetails() {
       return OrderDetail.of(this.id, this.userId, this.quantity, this.price);
    }
}
