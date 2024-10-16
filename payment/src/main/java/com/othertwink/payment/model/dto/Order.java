package com.othertwink.payment.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long id;
    private BigDecimal price;
    private String status;
}

