package com.vention.stock_market_share.repository;

import com.vention.stock_market_share.dto.SecurityInfoDTO;
import com.vention.stock_market_share.model.SecurityInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SecurityInfoRepository {

    private final DataSource dataSource;

    @Autowired
    public SecurityInfoRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void save(SecurityInfo securityInfo) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO security_info (username, password, user_id) VALUES (?, ?, ?)")) {
            preparedStatement.setString(1, securityInfo.getUsername());
            preparedStatement.setString(2, securityInfo.getPassword());
            preparedStatement.setLong(3, securityInfo.getUserId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public SecurityInfoDTO findById(Long id) {
        SecurityInfoDTO securityInfoDTO = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM security_info WHERE id = ?")) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    securityInfoDTO = mapRowToSecurityInfo(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return securityInfoDTO;
    }

    public List<SecurityInfoDTO> findAll() {
        List<SecurityInfoDTO> securityInfoDTOs = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM security_info")) {
            while (resultSet.next()) {
                SecurityInfoDTO securityInfoDTO = mapRowToSecurityInfo(resultSet);
                securityInfoDTOs.add(securityInfoDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return securityInfoDTOs;
    }

    public void update(SecurityInfoDTO securityInfoDTO) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE security_info SET username = ?, password = ? WHERE id = ?")) {
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
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM security_info WHERE id = ?")) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private SecurityInfoDTO mapRowToSecurityInfo(ResultSet resultSet) throws SQLException {
        SecurityInfoDTO securityInfoDTO = new SecurityInfoDTO();
        securityInfoDTO.setId(resultSet.getLong("id"));
        securityInfoDTO.setUsername(resultSet.getString("username"));
        securityInfoDTO.setPassword(resultSet.getString("password"));
        securityInfoDTO.setUserId(resultSet.getLong("user_id"));

        return securityInfoDTO;
    }
}
