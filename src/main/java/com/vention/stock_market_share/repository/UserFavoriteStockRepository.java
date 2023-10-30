package com.vention.stock_market_share.repository;

import com.vention.stock_market_share.model.Stock;
import com.vention.stock_market_share.model.User;
import com.vention.stock_market_share.model.UserFavoriteStock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class UserFavoriteStockRepository {
    @Autowired
    private DataSource dataSource;
    private String SQL_INSERT = "INSERT INTO UserFavoriteStock (user_id, stock_id) VALUES (?, ?)";
    private final String FIND_FaVORITE_STOCK = "SELECT s.id, s.symbol, s.name, s.currency, s.exchange, s.mic_code, s.country, s.type, s.date\n" +
            "FROM userfavoritestock ufs\n" +
            "         JOIN Stock s ON ufs.stock_id = s.id\n" +
            "WHERE ufs.user_id = ?;\n";
    private final String FIND_ALL_USERS = "SELECT u.id, u.firstname, u.lastname, u.email, u.age\n" +
            "FROM userfavoritestock ufs\n" +
            "         JOIN users u ON ufs.user_id = u.id" +
            "         group by u.id;";

    public void save(UserFavoriteStock favoriteStock) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT)) {
                preparedStatement.setLong(1, favoriteStock.getUserId());
                preparedStatement.setLong(2, favoriteStock.getStockId());
                preparedStatement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
                connection.rollback();
            }
        } catch (SQLException e) {
            log.error("error happened while saving to the database");
            e.printStackTrace();
        }

    }

    public List<Stock> getFavoriteStocks(Long userId) {
        List<Stock> stockList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_FaVORITE_STOCK);
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Stock stock = mapRowToStock(resultSet);
                stockList.add(stock);
            }
        } catch (SQLException e) {
            log.error("An error occurred while retrieving favorite stocks for user " + userId, e);
        }
        return stockList;

    }
    public List<User> getAllUsersWhoLikedStocks(){
        List<User> userList = new ArrayList<>();
        try(Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_ALL_USERS);
            while (resultSet.next()){
                User user = mapRowToUser(resultSet);
                userList.add(user);
            }

        } catch (SQLException e) {
            log.error("An error occurred while retrieving users who liked stocks", e);
        }
        return userList;
    }

    public boolean existsByUserIdAndStockId(Long userId, Long stockId) {
        String sql = "SELECT COUNT(*) FROM userfavoritestock WHERE user_id = ? AND stock_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
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

    private Stock mapRowToStock(ResultSet resultSet) throws SQLException {
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

    private User mapRowToUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setFirstname(resultSet.getString("firstname"));
        user.setLastname(resultSet.getString("lastname"));
        user.setEmail(resultSet.getString("email"));
        user.setAge(resultSet.getInt("age"));
        return user;
    }
}

