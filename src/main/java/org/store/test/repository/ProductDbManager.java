package org.store.test.repository;

import org.store.test.exception.ShopDbException;
import org.store.test.model.Product;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDbManager {
    private final DataSource dataSource;

    private final String PUT_QUERY = "INSERT INTO store.products(name, cost) VALUES(?,?) ";
    private final String GET_ID_QUERY = "SELECT * from store.products where id = ?";
    private final String GET_QUERY = "SELECT * from store.products";

    public ProductDbManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public synchronized Optional<Product> put(Product product) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(PUT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, product.getName());
            statement.setBigDecimal(2, product.getCost());
            statement.executeUpdate();
            final ResultSet generatedKeys = statement.getGeneratedKeys();
            if (!generatedKeys.next()) {
                return Optional.empty();
            }
            return Optional.of(new Product(
                    generatedKeys.getLong(1),
                    product.getName(),
                    product.getCost()
            ));
        } catch (SQLException e) {
            throw new ShopDbException(e.getMessage(), e.getCause());
        }
    }

    public synchronized Optional<Product> getById(long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ID_QUERY)) {
            statement.setLong(1, id);
            final ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return Optional.empty();
            }
            return Optional.of(new Product(
                    resultSet.getLong(1),
                    resultSet.getString(2),
                    resultSet.getBigDecimal(3)));
        } catch (SQLException e) {
            throw new ShopDbException(e.getMessage(), e.getCause());
        }
    }

    public List<Product> get() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_QUERY)) {
            List<Product> temp = new ArrayList<>();
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                temp.add(new Product(
                        resultSet.getLong(1),
                        resultSet.getString(2),
                        resultSet.getBigDecimal(3)));
            }
            return temp;
        } catch (SQLException e) {
            throw new ShopDbException(e.getMessage(), e.getCause());
        }
    }
}
