package com.vention.stock_market_share.repository;

import com.vention.stock_market_share.dto.UserDto;
import com.vention.stock_market_share.enums.Role;
import com.vention.stock_market_share.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class UserRepository {
    private final DataSource dataSource;
    private static final String SQL_GET_BY_USERNAME = "SELECT u.id, u.firstname, u.lastname, u.email, u.age, u.role, s.password, s.username FROM USERS u join SECURITY_INFO s ON s.user_id = u.id where s.username = ?";
    private final String SQL_GET_ALL = "SELECT * FROM USERS";
    private final String SQL_GET_BY_EMAIL = "SELECT * FROM Users WHERE email = ?";
    private final String SQL_GET_BY_ID = "SELECT * FROM Users WHERE id = ?";
    private final String SQL_INSERT = "INSERT INTO Users (firstname, lastname, email, age, role) VALUES (?, ?, ?, ?, ?) RETURNING id";
    private final String SQL_UPDATE = "UPDATE Users SET firstname = ?, lastname = ?, email = ?, age = ?, role = ? WHERE id = ?";
    private final String SQL_DELETE_BY_ID = "DELETE FROM Users WHERE id = ?";
    private final String DELETE_ALL = "DELETE FROM Users";
    private final String FIND_BY_ROLE = "SELECT * FROM USERS WHERE role = ?";

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
            log.error("error occurred while retrieving all users", e);
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
            log.error("error occurred while finding user by id");
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
                preparedStatement.setString(5, user.getRole().name());
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
            e.printStackTrace();
        }
        return 0;
    }

    public boolean update(User user) {
        int affectedRow = 0;
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE)) {
                preparedStatement.setString(1, user.getFirstname());
                preparedStatement.setString(2, user.getLastname());
                preparedStatement.setString(3, user.getEmail());
                preparedStatement.setInt(4, user.getAge());
                preparedStatement.setLong(5, user.getId());
                affectedRow = preparedStatement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                log.error("Error occurred while updating the user " + user.getFirstname() + ": " + e.getMessage());
                connection.rollback();
            }
        } catch (SQLException e) {
            log.error("Error occurred while updating the user " + user.getFirstname() + ": " + e.getMessage());
        }
        return affectedRow != 0;
    }

    public int delete(Long id) {
        int affectedRows = -1;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_BY_ID)) {
            preparedStatement.setLong(1, id);
            affectedRows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("error occurred while deleting the user" + ": " + e.getMessage());
        }
        return affectedRows;
    }

    public int deleteAll() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            return statement.executeUpdate(DELETE_ALL);
        } catch (SQLException e) {
            log.error("error occurred while deleting all of the users" + ": " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public User mapRowToUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setFirstname(resultSet.getString("firstname"));
        user.setLastname(resultSet.getString("lastname"));
        user.setEmail(resultSet.getString("email"));
        user.setAge(resultSet.getInt("age"));
        user.setRole(Role.valueOf(resultSet.getString("role")));
        return user;
    }

    public UserDto mapRowToUserDto(ResultSet resultSet) throws SQLException {
        UserDto userDto = new UserDto();
        userDto.setId(resultSet.getLong("id"));
        userDto.setFirstname(resultSet.getString("firstname"));
        userDto.setLastname(resultSet.getString("lastname"));
        userDto.setEmail(resultSet.getString("email"));
        userDto.setAge(resultSet.getInt("age"));
        userDto.setRole(Role.valueOf(resultSet.getString("role")));
        userDto.setUsername(resultSet.getString("username"));
        userDto.setPassword(resultSet.getString("password"));
        return userDto;
    }

    public User findByEmail(String email) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_BY_EMAIL)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRowToUser(resultSet);
                }
            }
        } catch (SQLException e) {
            log.error("Error occurred while retrieving the user by this email: " + email, e);
        }
        return null;
    }

    public long registerUser(User request) {
        long userId = -1;
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            User user = new User();
            user.setFirstname(request.getFirstname());
            user.setLastname(request.getLastname());
            user.setEmail(request.getEmail());
            user.setAge(request.getAge());
            user.setRole(Role.USER);
            userId = save(user);
            connection.commit();
        } catch (SQLException e) {
            log.error("error occurred while retrieving the user");
        }
        return userId;
    }

    public UserDto findByUsername(String username) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_BY_USERNAME)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRowToUserDto(resultSet);
                }
            }
        } catch (SQLException e) {
            log.error("error occurred while retrieving the user by email");
        }
        return null;
    }
}
