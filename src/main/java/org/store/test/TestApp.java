package org.store.test;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.store.test.exception.EntityNotFoundException;
import org.store.test.manager.*;
import org.store.test.property.PropertyLoader;
import org.store.test.property.SimplePropertiesLoader;
import org.store.test.repository.OrderDbManager;
import org.store.test.repository.ProductDbManager;
import org.store.test.repository.UserDbManager;
import org.store.test.service.order.OrderService;
import org.store.test.service.order.converter.OrderConverter;
import org.store.test.service.product.ProductService;
import org.store.test.service.product.converter.ProductConverter;
import org.store.test.service.user.UserService;
import org.store.test.service.order.converter.SimpleOrderConverter;
import org.store.test.service.product.converter.SimpleProductConverter;
import org.store.test.service.user.converter.SimpleUserConverter;
import org.store.test.service.user.converter.UserConverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class TestApp {

    public static void main(String[] args) {

        PropertyLoader propertyLoader = new SimplePropertiesLoader();
        final Properties properties = propertyLoader.load("server.properties");

        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(properties.getProperty("jdbcUrl"));
        hikariConfig.setUsername("root");
        hikariConfig.setPassword(properties.getProperty("password"));
        hikariConfig.setMaximumPoolSize(Integer.parseInt(properties.getProperty("maximumPoolSize")));
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);

        ProductConverter productConverter = new SimpleProductConverter();
        UserConverter userConverter = new SimpleUserConverter();
        OrderConverter orderConverter = new SimpleOrderConverter();

        UserService userService = new UserService(new UserDbManager(dataSource), userConverter);
        ProductService productService = new ProductService(new ProductDbManager(dataSource), productConverter);
        OrderService orderService = new OrderService(new OrderDbManager(dataSource), orderConverter);


        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));) {
            OrderMaker orderMaker = new SimpleOrderMaker(bufferedReader, productService, userService, orderService);
            OrderReceiver orderReceiver = new SimpleOrderReceiver(orderService, userService);
            ShopManager shopManager = new ShopManager(bufferedReader, orderMaker, orderReceiver);
            shopManager.start();
        } catch (EntityNotFoundException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
