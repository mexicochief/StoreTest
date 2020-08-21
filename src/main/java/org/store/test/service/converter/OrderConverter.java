package org.store.test.service.converter;

import org.store.test.dto.OrderDto;
import org.store.test.dto.ProductDto;
import org.store.test.model.Order;
import org.store.test.model.Product;

import java.util.Map;
import java.util.stream.Collectors;

public class OrderConverter {
    public OrderDto convert(Order order) {
        final Map<Product, Long> productDtoBuckets = order.getBucket();
        final Long orderPrice = productDtoBuckets
                .entrySet()
                .stream()
                .map(entry -> entry.getKey().getCost() * entry.getValue())
                .reduce(Long::sum)
                .orElseThrow(() -> new RuntimeException("Empty bucket"));
        final Map<ProductDto, Long> productBucket = productDtoBuckets
                .entrySet()
                .stream()
                .collect(Collectors.toMap(entry -> new ProductDto(entry.getKey().getId(), entry.getKey().getName(), entry.getKey().getCost()), Map.Entry::getValue));
        return new OrderDto(order.getId(), order.getUserId(), order.getDate(), productBucket, orderPrice);
    }

    public Order convert(OrderDto orderDto) {
        final Map<ProductDto, Long> productDtoBucket = orderDto.getBucket();
        final Long orderPrice = productDtoBucket
                .entrySet()
                .stream()
                .map(entry -> entry.getKey().getCost() * entry.getValue())
                .reduce(Long::sum)
                .orElseThrow(() -> new RuntimeException("Empty bucket"));
        final Map<Product, Long> productBucket = productDtoBucket
                .entrySet()
                .stream()
                .collect(Collectors.toMap(entry -> new Product(entry.getKey().getId(), entry.getKey().getName(), entry.getKey().getCost()), Map.Entry::getValue));
        return new Order(orderDto.getId(), orderDto.getUserId(), orderDto.getDate(), productBucket, orderPrice);

    }
}
