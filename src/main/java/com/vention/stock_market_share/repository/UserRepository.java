package com.vention.stock_market_share.repository;

import com.vention.stock_market_share.dto.UserDTO;
import com.vention.stock_market_share.dto.UserRegistrationDTO;
import com.vention.stock_market_share.model.SecurityInfo;
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
    private final String SQL_GET_BY_ID = "SELECT * FROM Users WHERE id = ?";
    private final String SQL_INSERT = "INSERT INTO Users (firstname, lastname, email, age) VALUES (?, ?, ?, ?) RETURNING id";
    private final String SQL_UPDATE = "UPDATE Users SET firstname = ?, lastname = ?, email = ?, age = ? WHERE id = ?";
    private final String SQL_DELETE_BY_ID = "DELETE FROM Users WHERE id = ?";

    private final String DELETE_ALL = "DELETE FROM Users";

    public List<UserDTO> findAll() {
        List<UserDTO> users = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL_GET_ALL)) {

            while (resultSet.next()) {
                UserDTO user = mapRowToUser(resultSet);
                users.add(user);
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return users;
    }

    public UserDTO findById(Long id) {
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

    public void save(User user) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            setPreparedStatementParameters(preparedStatement, user);
            int affectedRows = preparedStatement.executeUpdate();
            Long id = 0l;
            if (affectedRows == 1) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        id = generatedKeys.getLong(1);
                    }
                }
            }
            user.setId(id);
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public void update(User user) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE)) {
            setPreparedStatementParameters(preparedStatement, user);
            preparedStatement.setLong(5, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            handleSQLException(e);
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

    private UserDTO mapRowToUser(ResultSet resultSet) throws SQLException {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(resultSet.getLong("id"));
        userDTO.setFirstname(resultSet.getString("firstname"));
        userDTO.setLastname(resultSet.getString("lastname"));
        userDTO.setEmail(resultSet.getString("email"));
        userDTO.setAge(resultSet.getInt("age"));
        return userDTO;
    }

    private void setPreparedStatementParameters(PreparedStatement preparedStatement, User user) throws SQLException {
        preparedStatement.setString(1, user.getFirstname());
        preparedStatement.setString(2, user.getLastname());
        preparedStatement.setString(3, user.getEmail());
        preparedStatement.setInt(4, user.getAge());
    }

    public void registerUser(UserRegistrationDTO registrationDTO) {
        User user = new User();
        user.setFirstname(registrationDTO.getFirstname());
        user.setLastname(registrationDTO.getLastname());
        user.setEmail(registrationDTO.getEmail());
        user.setAge(registrationDTO.getAge());


        SecurityInfo securityInfo = new SecurityInfo();
        securityInfo.setUsername(registrationDTO.getUsername());
        securityInfo.setPassword(registrationDTO.getPassword());

        save(user);
        securityInfo.setUser(user);
        securityInfoRepository.save(securityInfo);

    }

    private void handleSQLException(SQLException e) {
        e.printStackTrace();
    }
}
