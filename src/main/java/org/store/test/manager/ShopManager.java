package org.store.test.manager;

import de.vandermeer.asciitable.AsciiTable;
import org.store.test.dto.OrderDto;
import org.store.test.dto.UserDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

public class ShopManager {
    private final BufferedReader bufferedReader;
    private final OrderMaker orderMaker;
    private final OrderReceiver orderReceiver;

    public ShopManager(BufferedReader bufferedReader, OrderMaker orderMaker, OrderReceiver orderReciver) {
        this.bufferedReader = bufferedReader;
        this.orderMaker = orderMaker;
        this.orderReceiver = orderReciver;
    }

    public void start() {
        System.out.println("1 сделать заказ");
        System.out.println("2 вывести заказы");
        long b = 0;
        try {
            b = Long.parseLong(bufferedReader.readLine());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        if (b == 1) {
            final OrderDto orderDto = orderMaker.makeOrder();
            orderDto.getBucket().forEach((key, value) -> System.out.println(key.getName() + " " + value));
        } else if (b == 2) {
            final Map<OrderDto, UserDto> ordersUsers = orderReceiver.getOrder();
            AsciiTable table = new AsciiTable();
            table.addRow("Id", "First name", "Last name", "Date", "Product", "Cost", "Quantity");
            table.setPaddingBottom(2);
            for (Map.Entry<OrderDto, UserDto> orderUser : ordersUsers.entrySet()) {
                final OrderDto order = orderUser.getKey();
                final UserDto user = orderUser.getValue();
                order.getBucket()
                        .forEach((product, quantity) -> table.addRow(
                                order.getId(),
                                user.getFirstName(),
                                user.getLastName(),
                                order.getDate(),
                                product.getName(),
                                (product.getCost().multiply(new BigDecimal(quantity))),
                                quantity));
            }
            final String render = table.render();
            System.out.println(render);
        } else {
            System.out.println("Такого номера нет");
        }
    }


}
