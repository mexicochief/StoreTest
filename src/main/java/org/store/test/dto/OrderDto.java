package org.store.test.dto;

import lombok.Data;

import java.sql.Date;
import java.util.Map;

@Data
public class OrderDto {
    private final long id;
    private final long userId;
    private final Date date;
    private final Map<ProductDto, Long> bucket;
    private final long sum;
}
