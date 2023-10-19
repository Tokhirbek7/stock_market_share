package com.vention.stock_market_share.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;

import com.vention.stock_market_share.model.Stock;
import org.springframework.stereotype.Repository;

@Repository
public class StockDataRepository {
    private final DataSource dataSource;
    private final String SLQ_INSERT ="INSERT INTO Stock (symbol, name, currency, exchange, mic_code, country, type, date) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    public StockDataRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    public void save(Stock stock) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(SLQ_INSERT)) {
                preparedStatement.setString(1, stock.getSymbol());
                preparedStatement.setString(2, stock.getName());
                preparedStatement.setString(3, stock.getCurrency());
                preparedStatement.setString(4, stock.getExchange());
                preparedStatement.setString(5, stock.getMicCode());
                preparedStatement.setString(6, stock.getCountry());
                preparedStatement.setString(7, stock.getType());
                preparedStatement.setDate(8, new java.sql.Date(stock.getDate().getTime()));
                preparedStatement.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException closeException) {
                closeException.printStackTrace();
            }
        }
    }
}
