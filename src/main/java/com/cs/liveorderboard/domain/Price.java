package com.cs.liveorderboard.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Price {
    private BigDecimal amount;
    private Currency currency;

    public Price() {}
}
