package com.vention.stock_market_share.token;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {
    private final DataSource dataSource;
    private static final int TOKEN_LENGTH = 32;

    private final String SQL_INSERT = "INSERT INTO token (body, createdat) VALUES (?, ?); ";
    private final String SQL_FIND = "select * from token order by id desc limit 1";

    public String generateToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] tokenBytes = new byte[TOKEN_LENGTH];
        secureRandom.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }

    public void save(String token) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT);
            preparedStatement.setString(1, token);
            preparedStatement.setDate(2, new java.sql.Date(new Date().getTime()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("error occurred while saving into database", e);
        }
    }

    public Token findToken() {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Token token = new Token();
                token.setId((long) resultSet.getInt("id"));
                token.setBody(resultSet.getString("body"));
                token.setCreatedAt(resultSet.getDate("createdat"));
                return token;
            }
        } catch (SQLException e) {
            log.error("Error occurred while retrieving the token from the database", e);
        }
        return null;
    }
}
