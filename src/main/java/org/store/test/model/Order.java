package org.store.test.model;

import lombok.Data;

import java.sql.Date;
import java.util.Map;

@Data
public class Order {
    private final long id;
    private final long userId;
    private final Date date;
    private final Map<Product, Long> bucket;
    private final long sum;
}
