package org.store.test.model;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Product {
    private long id;
    private String name;
    private long cost;

    public Product(long id, String name, long cost) {
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

    public long getCost() {
        return cost;
    }
}
