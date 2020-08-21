package org.store.test.repository;

import org.store.test.exception.ShopDbException;
import org.store.test.model.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDbManager {
    private final DataSource dataSource;

    private final String PUT_QUERY = "INSERT INTO store.users(first_name, last_name) VALUES(?,?) ";
    private final String GET_ID_QUERY = "SELECT * from store.users where id = ?";
    private final String GET_QUERY = "SELECT * from store.users";

    public UserDbManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public synchronized Optional<User> put(User userEntity) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(PUT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, userEntity.getFirstName());
            statement.setString(2, userEntity.getLastName());
            statement.executeUpdate();
            final ResultSet generatedKeys = statement.getGeneratedKeys();
            if (!generatedKeys.next()) {
                return Optional.empty();
            }
            return Optional.of(new User(
                    generatedKeys.getLong(1),
                    userEntity.getFirstName(),
                    userEntity.getLastName()
            ));
        } catch (SQLException e) {
            throw new ShopDbException(e.getMessage(), e.getCause());
        }
    }

    public synchronized Optional<User> getById(long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ID_QUERY)) {
            statement.setLong(1, id);
            final ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return Optional.empty();
            }
            return Optional.of(new User(
                    resultSet.getLong(1),
                    resultSet.getString(2),
                    resultSet.getString(3)));
        } catch (SQLException e) {
            throw new ShopDbException(e.getMessage(), e.getCause());
        }
    }

    public List<User> get() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_QUERY)) {
            List<User> temp = new ArrayList<>();
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                temp.add(new User(
                        resultSet.getLong(1),
                        resultSet.getString(2),
                        resultSet.getString(3)));
            }
            return temp;
        } catch (SQLException e) {
            throw new ShopDbException(e.getMessage(), e.getCause());
        }
    }
}
