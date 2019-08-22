package com.cs.liveorderboard.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor(staticName = "of")
public class OrderResponse {
    private OrderType orderType;
    private List<OrderDetail> orders;
}
