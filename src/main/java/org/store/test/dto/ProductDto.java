package org.store.test.dto;

import lombok.Data;

@Data
public class ProductDto {
    private final long id;
    private final String name;
    private final long cost;
}
