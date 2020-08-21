package org.store.test.manager;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.translators.TargetTranslator;
import de.vandermeer.translation.targets.Text2Html;
import org.store.test.dto.OrderDto;
import org.store.test.dto.UserDto;
import org.store.test.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
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
            table.addRow("Id", "Last name", "First name", "Date", "Product", "Cost", "Quantity");
            table.setPaddingBottom(2);
            for (Map.Entry<OrderDto, UserDto> orderUser : ordersUsers.entrySet()) {
                final OrderDto order = orderUser.getKey();
                final UserDto user = orderUser.getValue();
                order.getBucket()
                        .forEach((product, quantity) -> table.addRow(
                                order.getId(),
                                user.getLastName(),
                                user.getFirstName(),
                                order.getDate(),
                                product.getName(),
                                (product.getCost() * quantity),
                                quantity));
            }
            final String render = table.render();
            System.out.println(render);
        } else {
            System.out.println("Такого номера нет");
        }
    }


}
