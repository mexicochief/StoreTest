package org.store.test.service.order.converter;

import org.store.test.dto.OrderDto;
import org.store.test.model.Order;

public interface OrderConverter {
    OrderDto convert(Order order);

    Order convert(OrderDto orderDto);
}
