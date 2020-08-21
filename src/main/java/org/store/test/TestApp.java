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
import org.store.test.service.OrderService;
import org.store.test.service.ProductService;
import org.store.test.service.UserService;
import org.store.test.service.converter.OrderConverter;
import org.store.test.service.converter.ProductConverter;
import org.store.test.service.converter.UserConverter;

import java.io.BufferedReader;
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

        ProductConverter productConverter = new ProductConverter();
        UserConverter userConverter = new UserConverter();
        OrderConverter orderConverter = new OrderConverter();

        UserService userService = new UserService(new UserDbManager(dataSource), userConverter);
        ProductService productService = new ProductService(new ProductDbManager(dataSource), productConverter);
        OrderService orderService = new OrderService(new OrderDbManager(dataSource), orderConverter);


        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        try {
            OrderMaker orderMaker = new SimpleOrderMaker(bufferedReader, productService, userService, orderService);
            OrderReceiver orderReceiver = new SimpleOrderReceiver(orderService, userService);
            ShopManager shopManager = new ShopManager(bufferedReader, orderMaker, orderReceiver);
            shopManager.start();
        } catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

}
