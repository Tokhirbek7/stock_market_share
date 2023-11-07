package com.vention.stock_market_share.repository;

import com.vention.stock_market_share.model.SecurityInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class SecurityInfoRepository {
    private final DataSource dataSource;
    private final PasswordEncoder passwordEncoder;
    private final String SQL_SAVE = "INSERT INTO security_info (username, password, user_id) VALUES (?, ?, ?)";
    private final String SQL_FIND_BY_ID = "SELECT * FROM security_info WHERE user_id = ?";
    private final String SQL_FIND_BY_USERNAME = "SELECT * FROM security_info WHERE username = ?";
    private final String FIND_ALL = "SELECT * FROM security_info";
    private final String DELETE_BY_ID = "DELETE FROM security_info WHERE user_id = ?";
    private final String UPDATE_BY_ID = "UPDATE security_info SET username = ?, password = ? WHERE user_id = ?";

    public boolean save(SecurityInfo securityInfo, Long userId) {
        int affected = 0;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE)) {
            connection.setAutoCommit(false);

            preparedStatement.setString(1, securityInfo.getUsername());
            preparedStatement.setString(2, passwordEncoder.encode(securityInfo.getPassword()));
            preparedStatement.setLong(3, userId);

            affected = preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            log.error("Error occurred while saving this" + securityInfo.getUserId() + "the database", e);
        }
        return affected != 0;
    }

    public SecurityInfo findByUsername(String username) {
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
            log.error(e.getMessage());
        }
        return securityInfo;
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
            log.error("error occurred while retrieving security info by id " + id, e);
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
            log.error("Error occurred while retrieving all security info", e);
        }
        return securityInfo;
    }

    public boolean update(Long id, SecurityInfo securityInfoDTO) {
        int affectedRow = -1;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BY_ID)) {
            preparedStatement.setString(1, securityInfoDTO.getUsername());
            preparedStatement.setString(2, passwordEncoder.encode(securityInfoDTO.getPassword()));
            preparedStatement.setLong(3, id);
            affectedRow = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error occurred while updating security info", e);
        }
        return affectedRow > 0;
    }

    public boolean delete(Long userId) {
        int affectedRow = -1;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID)) {
            preparedStatement.setLong(1, userId);
            affectedRow = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("error occurred while deleting the security info by this user_id " + userId, e);
        }
        return affectedRow > 0;
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
