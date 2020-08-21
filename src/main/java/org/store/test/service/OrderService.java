package org.store.test.service;

import org.store.test.dto.OrderDto;
import org.store.test.exception.EntityNotFoundException;
import org.store.test.model.Order;
import org.store.test.repository.OrderDbManager;
import org.store.test.service.converter.OrderConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderService {
    private final OrderDbManager orderDbManager;
    private final OrderConverter orderConverter;

    public OrderService(OrderDbManager orderDbManager, OrderConverter orderConverter) {
        this.orderDbManager = orderDbManager;
        this.orderConverter = orderConverter;
    }

    public OrderDto put(OrderDto orderDto) {
        orderConverter.convert(orderDto);


        final Order order = orderDbManager.put(orderConverter.convert(orderDto))
                .orElseThrow(() -> new EntityNotFoundException("Order"));
        return orderConverter.convert(order);
    }

    public List<OrderDto> get() {
        final List<Order> orders = orderDbManager.get();
        return orders.stream().map(orderConverter::convert).collect(Collectors.toList());
    }

    public OrderDto getById(long id) {
        final Order order = orderDbManager.getById(id).orElseThrow(() -> new EntityNotFoundException("Order"));
        return orderConverter.convert(order);
    }
}
