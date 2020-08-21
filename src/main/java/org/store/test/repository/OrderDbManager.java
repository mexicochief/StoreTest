package org.store.test.repository;

import org.store.test.exception.ShopDbException;
import org.store.test.model.Order;
import org.store.test.model.Product;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class OrderDbManager {
    private final DataSource dataSource;

    private final String PUT_QUERY_ORDER = "INSERT INTO store.orders(user_id, order_date, sum) VALUES(?, CURDATE(),?) ";
    private final String PUT_QUERY_ORDER_PRODUCT = "INSERT INTO store.orderproduct(order_id, product_id) VALUES(?,?)";
    private final String GET_BY_ID_QUERY = "SELECT * from store.orders where id = ?";
    private final String GET_ORDERED_PRODUCT_QUERY = "select id,product_name,cost,count(id) from store.orderproduct " +
            "inner join store.products on store.orderproduct.product_id = store.products.id " +
            "where order_id = ? group by id";
    private final String GET_QUERY = "SELECT * from store.orders order by order_date";

    public OrderDbManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public synchronized Optional<Order> put(Order order) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement putStatement = connection.prepareStatement(PUT_QUERY_ORDER, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement updateStatement = connection.prepareStatement(PUT_QUERY_ORDER_PRODUCT)) {
            connection.setAutoCommit(false);
            putStatement.setLong(1, order.getUserId());
            putStatement.setBigDecimal(2, order.getSum());
            putStatement.executeUpdate();
            final ResultSet generatedKeys = putStatement.getGeneratedKeys();
            if (!generatedKeys.next()) {
                return Optional.empty();
            }
            final Map<Product, Long> bucket = order.getBucket();
            for (Map.Entry<Product, Long> productLongEntry : bucket.entrySet()) {
                Long value = productLongEntry.getValue();
                while (value != 0) {
                    updateStatement.setLong(1, generatedKeys.getLong(1));
                    updateStatement.setLong(2, productLongEntry.getKey().getId());
                    updateStatement.executeUpdate();
                    value--;
                }
            }
            connection.commit();
            return getById(generatedKeys.getLong(1));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new ShopDbException(e.getMessage(), e.getCause());
        }
    }

    public synchronized Optional<Order> getById(long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement getOrderByIdPs = connection.prepareStatement(GET_BY_ID_QUERY);
             PreparedStatement getProductsPs = connection.prepareStatement(GET_ORDERED_PRODUCT_QUERY)) {
            Map<Product, Long> bucket = new HashMap<>();

            getOrderByIdPs.setLong(1, id);
            final ResultSet orderResultSet = getOrderByIdPs.executeQuery();
            if (!orderResultSet.next()) {
                return Optional.empty();
            }

            getProductsPs.setLong(1, orderResultSet.getLong(1));
            final ResultSet productsResultSet = getProductsPs.executeQuery();
            while (productsResultSet.next()) {
                final long productId = productsResultSet.getLong(1);
                final Product product
                        = new Product(productId, productsResultSet.getString(2), productsResultSet.getBigDecimal(3));
                bucket.put(product, productsResultSet.getLong(4));
            }
            return Optional.of(new Order(
                    orderResultSet.getLong(1),
                    orderResultSet.getLong(2),
                    orderResultSet.getDate(3),
                    bucket,
                    orderResultSet.getBigDecimal(4)));
        } catch (SQLException e) {
            throw new ShopDbException(e.getMessage(), e.getCause());
        }
    }

    public List<Order> get() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement getStatement = connection.prepareStatement(GET_QUERY);
             PreparedStatement joinStatement = connection.prepareStatement(GET_ORDERED_PRODUCT_QUERY)) {
            List<Order> orders = new ArrayList<>();
            final ResultSet orderResultSet = getStatement.executeQuery();
            while (orderResultSet.next()) {
                Map<Product, Long> bucket = new HashMap<>();
                final long orderID = orderResultSet.getLong(1);
                joinStatement.setLong(1, orderID);
                final ResultSet productRs = joinStatement.executeQuery();
                while (productRs.next()) {
                    final long productId = orderResultSet.getLong(1);
                    Product product = new Product(productId, productRs.getString(2), productRs.getBigDecimal(3));
                    bucket.put(product, productRs.getLong(4));
                }
                orders.add(new Order(orderID,
                        orderResultSet.getLong(2),
                        orderResultSet.getDate(3),
                        bucket, orderResultSet.getBigDecimal(4)));
            }
            return orders;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new ShopDbException(e.getMessage(), e.getCause());
        }
    }
}
