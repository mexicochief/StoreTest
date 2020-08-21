package org.store.test.manager;

import org.store.test.dto.OrderDto;
import org.store.test.dto.UserDto;
import org.store.test.service.OrderService;
import org.store.test.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleOrderReceiver implements OrderReceiver {
    private final OrderService orderService;
    private final UserService userService;

    public SimpleOrderReceiver(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @Override
    public Map<OrderDto, UserDto> getOrder() {
        Map<OrderDto, UserDto> orderUsers = new HashMap<>();
        final List<OrderDto> orderDtos = orderService.get();
        orderDtos.forEach(orderDto -> orderUsers.put(orderDto, userService.getById(orderDto.getUserId())));
        return orderUsers;
    }
}
