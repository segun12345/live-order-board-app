package com.cs.liveorderboard.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Quantity {
    private Double value;
    private Unit unit;

    public Quantity() {}
}
