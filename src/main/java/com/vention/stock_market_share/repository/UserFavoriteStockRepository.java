package com.vention.stock_market_share.repository;

import com.vention.stock_market_share.model.Stock;
import com.vention.stock_market_share.model.User;
import com.vention.stock_market_share.model.UserFavoriteStock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserFavoriteStockRepository {
    private final DataSource dataSource;
    private final StockDataRepository stockDataRepository;
    private final UserRepository userRepository;
    private String SQL_INSERT = "INSERT INTO UserFavoriteStock (user_id, stock_id) VALUES (?, ?)";
    private final String FIND_FAVORITE_STOCK = "SELECT s.id, s.symbol, s.name, s.currency, s.exchange, s.mic_code, s.country, s.type, s.date\n" +
            "FROM userfavoritestock ufs\n" +
            "         JOIN Stock s ON ufs.stock_id = s.id\n" +
            "WHERE ufs.user_id = ?;\n";
    private final String FIND_ALL_USERS = "SELECT u.id, u.firstname, u.lastname, u.email, u.age, u.role\n" +
            "FROM userfavoritestock ufs\n" +
            "         JOIN users u ON ufs.user_id = u.id" +
            "         group by u.id;";
    private final String SQL_COUNT_USER_STOCKS = "SELECT COUNT(*) FROM userfavoritestock WHERE user_id = ? AND stock_id = ?";

    public boolean save(UserFavoriteStock favoriteStock) {
        int affectedRow =-1;
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT)) {
                preparedStatement.setLong(1, favoriteStock.getUserId());
                preparedStatement.setLong(2, favoriteStock.getStockId());
                affectedRow = preparedStatement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                log.error("error happened while saving to the database", e);
                connection.rollback();
            }
        } catch (SQLException e) {
            log.error("error happened while saving to the database", e);
        }
        return affectedRow!=0;
    }

    public List<Stock> getFavoriteStocks(Long userId) {
        List<Stock> stockList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_FAVORITE_STOCK);
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Stock stock = stockDataRepository.mapRowToStock(resultSet);
                stockList.add(stock);
            }
        } catch (SQLException e) {
            log.error("An error occurred while retrieving favorite stocks for user " + userId, e);
        }
        return stockList;
    }

    public List<User> getAllUsersWhoLikedStocks() {
        List<User> userList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_ALL_USERS);
            while (resultSet.next()) {
                User user = userRepository.mapRowToUser(resultSet);
                userList.add(user);
            }
        } catch (SQLException e) {
            log.error("An error occurred while retrieving users who liked stocks", e);
        }
        return userList;
    }

    public boolean existsByUserIdAndStockId(Long userId, Long stockId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_COUNT_USER_STOCKS)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, stockId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            log.error("An error occurred", e);
        }
        return false;
    }
}

