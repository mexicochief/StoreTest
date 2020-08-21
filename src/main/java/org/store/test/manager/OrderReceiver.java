package org.store.test.manager;

import org.store.test.dto.OrderDto;
import org.store.test.dto.UserDto;

import java.util.Map;

public interface OrderReceiver {
    Map<OrderDto, UserDto> getOrder();
}
