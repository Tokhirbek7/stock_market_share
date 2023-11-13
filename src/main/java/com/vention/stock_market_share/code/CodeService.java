package com.vention.stock_market_share.code;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class CodeService {
    private final DataSource dataSource;
    private final String SQL_INSERT = "INSERT INTO code (code, email) VALUES (?, ?); ";

    public String generateCode() {
        Random random = new Random();
        int min = 10000000;
        int max = 99999999;
        int generatedCode = random.nextInt((max - min) + 1) + min;
        return String.valueOf(generatedCode);
    }

    public boolean save(String code, String email) {
        int affectedRow = -1;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT);
            preparedStatement.setString(1, code);
            preparedStatement.setString(2, email);
            affectedRow = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("error occurred while saving into database", e);
        }
        return affectedRow > 0;
    }

}
