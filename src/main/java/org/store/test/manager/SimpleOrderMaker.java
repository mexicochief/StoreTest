package org.store.test.manager;

import org.store.test.dto.OrderDto;
import org.store.test.dto.ProductDto;
import org.store.test.dto.UserDto;
import org.store.test.service.order.OrderService;
import org.store.test.service.product.ProductService;
import org.store.test.service.user.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleOrderMaker implements OrderMaker {
    private final BufferedReader bufferedReader;
    private final ProductService productService;
    private final UserService userService;
    private final OrderService orderService;

    public SimpleOrderMaker(BufferedReader bufferedReader, ProductService productService, UserService userService, OrderService orderService) {
        this.bufferedReader = bufferedReader;
        this.productService = productService;
        this.userService = userService;
        this.orderService = orderService;
    }

    @Override
    public OrderDto makeOrder() {
        int userId = choseUser(userService.get());

        Map<ProductDto, Long> orderDtoIntegerMap = new HashMap<>();
        boolean flag = true;
        while (flag) {
            final long productId = choseProduct(productService.get());
            final ProductDto optionalProduct = productService.getById(productId);
            final long quantity = choseQuantity();
            System.out.println("Хотите другие Y/N");
            flag = isEnd();
            orderDtoIntegerMap.put(optionalProduct, quantity);
        }
        OrderDto orderDto = new OrderDto(0, userId, new Date(0), orderDtoIntegerMap, new BigDecimal(0));

        return orderService.put(orderDto);
    }

    public int choseUser(List<UserDto> users) {
        System.out.println("Выберете пользователя");
        for (UserDto user : users) {
            System.out.println(user.getId() + " " + user.getLastName() + " " + user.getFirstName());
        }
        int i = 0;
        try {
            String productStr = bufferedReader.readLine();
            i = Integer.parseInt(productStr);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Неверный символ");
            return choseUser(users);
        }
        if (i > users.size() || i < 0) {
            System.out.println("Неверный id");
            return choseUser(users);
        }
        return i;
    }

    public int choseProduct(List<ProductDto> products) {
        System.out.println("Выберете продукт");
        products.forEach(product -> System.out.println(product.getId() + " " + product.getName() + " " + product.getCost()));
        int i = 0;
        try {
            String productStr = bufferedReader.readLine();
            i = Integer.parseInt(productStr);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Неверный символ");
            return choseProduct(products);
        }

        if (i > products.size() || i < 0) {
            System.out.println("Неверный id");
            return choseProduct(products);
        }
        return i;
    }

    public long choseQuantity() {
        System.out.println("Выберете количество");
        long i = 0;
        try {
            String productStr = bufferedReader.readLine();
            i = Integer.parseInt(productStr);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Неверный символ");
            return choseQuantity();
        }
        return i;
    }

    public boolean isEnd() {
        try {
            return bufferedReader.readLine().toLowerCase().equals("y");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
