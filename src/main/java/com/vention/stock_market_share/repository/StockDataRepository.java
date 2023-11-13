package com.vention.stock_market_share.repository;

import com.vention.stock_market_share.model.Stock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class StockDataRepository {
    private final DataSource dataSource;
    private final String SQL_INSERT = "INSERT INTO Stock (symbol, name, currency, exchange, mic_code, country, type, date) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private final String FIND_BY_ID = "SELECT * from Stock where id = ?";
    private final String FIND_BY_DATE_SYMBOL = "SELECT * FROM stock WHERE symbol = ? AND date = ?";

    public void save(Stock stock) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT)) {
                preparedStatement.setString(1, stock.getSymbol());
                preparedStatement.setString(2, stock.getName());
                preparedStatement.setString(3, stock.getCurrency());
                preparedStatement.setString(4, stock.getExchange());
                preparedStatement.setString(5, stock.getMicCode());
                preparedStatement.setString(6, stock.getCountry());
                preparedStatement.setString(7, stock.getType());
                preparedStatement.setDate(8, new java.sql.Date(stock.getDate().getTime()));
                preparedStatement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                log.error("Error occurred while executing SQL statement", e);
                connection.rollback();
            }
        } catch (SQLException e) {
            log.error("Error occurred while getting a database connection", e);
        }
    }

    public Optional<Stock> findStockById(Long stockId) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
                preparedStatement.setLong(1, stockId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return Optional.ofNullable((mapRowToStock(resultSet)));
                    }
                }
            }
        } catch (SQLException e) {
            log.error("Error while finding stock by ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public List<Stock> getStocksByDateAndSymbol(String symbol, Date date) {
        List<Stock> stocks = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_DATE_SYMBOL)) {
            statement.setString(1, symbol);
            statement.setDate(2, new java.sql.Date(date.getTime()));
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Stock stock = mapRowToStock(resultSet);
                    stocks.add(stock);
                }
            }
        } catch (SQLException e) {
            log.error("An error occurred while retrieving the stock by date and symbol", e);
        }
        return stocks;
    }

    public Stock mapRowToStock(ResultSet resultSet) throws SQLException {
        Stock stock = new Stock();
        stock.setId(resultSet.getLong("id"));
        stock.setSymbol(resultSet.getString("symbol"));
        stock.setName(resultSet.getString("name"));
        stock.setCurrency(resultSet.getString("currency"));
        stock.setExchange(resultSet.getString("exchange"));
        stock.setMicCode(resultSet.getString("mic_code"));
        stock.setCountry(resultSet.getString("country"));
        stock.setType(resultSet.getString("type"));
        stock.setDate(resultSet.getDate("date"));
        return stock;
    }
}
