package org.store.test.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDto {
    private final long id;
    private final String name;
    private final BigDecimal cost;
}
