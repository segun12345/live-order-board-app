package com.cs.liveorderboard.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor(staticName = "of")
public class OrderDetail {

    private String id;
    private String userId;
    private Quantity quantity;
    private Price price;


    public BigDecimal getPriceAmount () {
       return this.getPrice().getAmount();
    }
}
