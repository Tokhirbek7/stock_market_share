package com.vention.stock_market_share.repository;

import com.vention.stock_market_share.model.SecurityInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SecurityInfoRepository {
    @Autowired
    private DataSource dataSource;
    private final String SQL_SAVE = "INSERT INTO security_info (username, password, user_id) VALUES (?, ?, ?)";
    private final String SQL_FIND_BY_ID = "SELECT * FROM security_info WHERE id = ?";
    private final String SQL_FIND_BY_USERNAME = "SELECT * FROM security_info WHERE username = ?";
    private final String FIND_ALL = "SELECT * FROM security_info";
    private final String DELETE_BY_ID = "DELETE FROM security_info WHERE id = ?";
    private final String UPDATE_BY_ID = "UPDATE security_info SET username = ?, password = ? WHERE user_id = ?";

    public void save(SecurityInfo securityInfo, Long userId) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE)) {
                preparedStatement.setString(1, securityInfo.getUsername());
                preparedStatement.setString(2, securityInfo.getPassword());
                preparedStatement.setLong(3, userId);
                preparedStatement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String findByUsername(String username) {
        SecurityInfo securityInfo = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_USERNAME)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    securityInfo = mapRowToSecurityInfo(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (securityInfo != null) {
            return securityInfo.getUsername();
        } else {
            return "Username is not found";
        }

    }


    public SecurityInfo findById(Long id) {
        SecurityInfo securityInfo = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_ID)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    securityInfo = mapRowToSecurityInfo(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return securityInfo;
    }

    public List<SecurityInfo> findAll() {
        List<SecurityInfo> securityInfo = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(FIND_ALL)) {
            while (resultSet.next()) {
                SecurityInfo securityInfoDTO = mapRowToSecurityInfo(resultSet);
                securityInfo.add(securityInfoDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return securityInfo;
    }


    public void update(SecurityInfo securityInfoDTO) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BY_ID)) {
            preparedStatement.setString(1, securityInfoDTO.getUsername());
            preparedStatement.setString(2, securityInfoDTO.getPassword());
            preparedStatement.setLong(3, securityInfoDTO.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isValidInput(SecurityInfo securityInfo) {
        return securityInfo.getUsername() != null
                && securityInfo.getPassword() != null;


    }

    private SecurityInfo mapRowToSecurityInfo(ResultSet resultSet) throws SQLException {
        SecurityInfo securityInfo = new SecurityInfo();
        securityInfo.setId(resultSet.getLong("id"));
        securityInfo.setUsername(resultSet.getString("username"));
        securityInfo.setPassword(resultSet.getString("password"));
        securityInfo.setUserId(resultSet.getLong("user_id"));
        return securityInfo;
    }
}
