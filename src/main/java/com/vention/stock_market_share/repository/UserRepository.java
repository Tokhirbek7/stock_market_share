package com.vention.stock_market_share.repository;

import com.vention.stock_market_share.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private SecurityInfoRepository securityInfoRepository;

    private final String SQL_GET_ALL = "SELECT * FROM Users";
    private final String SQL_GET_BY_EMAIL = "SELECT * FROM Users WHERE email = ?";
    private final String SQL_GET_BY_ID = "SELECT * FROM Users WHERE id = ?";
    private final String SQL_INSERT = "INSERT INTO Users (firstname, lastname, email, age) VALUES (?, ?, ?, ?) RETURNING id";
    private final String SQL_UPDATE = "UPDATE Users SET firstname = ?, lastname = ?, email = ?, age = ? WHERE id = ?";
    private final String SQL_DELETE_BY_ID = "DELETE FROM Users WHERE id = ?";
    private final String DELETE_ALL = "DELETE FROM Users";

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL_GET_ALL)) {

            while (resultSet.next()) {
                User user = mapRowToUser(resultSet);
                users.add(user);
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return users;
    }

    public User findById(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_BY_ID)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRowToUser(resultSet);
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return null;
    }

    public long save(User user) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            if (user.getEmail() != null) {
                preparedStatement.setString(1, user.getFirstname());
                preparedStatement.setString(2, user.getLastname());
                preparedStatement.setString(3, user.getEmail());
                preparedStatement.setInt(4, user.getAge());
                int affectedRows = preparedStatement.executeUpdate();
                long id = 0;
                if (affectedRows == 1) {
                    try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            id = generatedKeys.getLong(1);
                        }
                    }
                }
                user.setId(id);
                return user.getId();
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return 0;
    }

    public void update(User user) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE)) {
                preparedStatement.setString(1, user.getFirstname());
                preparedStatement.setString(2, user.getLastname());
                preparedStatement.setString(3, user.getEmail());
                preparedStatement.setInt(4, user.getAge());
                preparedStatement.setLong(5, user.getId());
                preparedStatement.executeUpdate();
            }

            try {
                if (connection != null) {
                    connection.commit();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            handleSQLException(e);
            rollbackTransaction(connection);
        } finally {
            closeConnection(connection);
        }
    }

    public void delete(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_BY_ID)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public void deleteAll() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(DELETE_ALL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private User mapRowToUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setFirstname(resultSet.getString("firstname"));
        user.setLastname(resultSet.getString("lastname"));
        user.setEmail(resultSet.getString("email"));
        user.setAge(resultSet.getInt("age"));
        return user;
    }


    public String findByEmail(String email) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_BY_EMAIL)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRowToUser(resultSet).getEmail();
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return null;
    }

    public long registerUser(User registrationDTO) {
        long userId = -1;
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            User user = new User();
            user.setFirstname(registrationDTO.getFirstname());
            user.setLastname(registrationDTO.getLastname());
            user.setEmail(registrationDTO.getEmail());
            user.setAge(registrationDTO.getAge());
            userId = save(user);
            connection.commit();
        } catch (SQLException e) {
            handleSQLException(e);
        }

        return userId;
    }




    private void rollbackTransaction(Connection connection) {
        try {
            if (connection != null) {
                connection.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleSQLException(SQLException e) {
        e.printStackTrace();
    }
}
