package org.store.test.model;

import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode
public class Product {
    private long id;
    private String name;
    private BigDecimal cost;

    public Product(long id, String name, BigDecimal cost) {
        this.id = id;
        this.name = name;
        this.cost = cost;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getCost() {
        return cost;
    }
}
