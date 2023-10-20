package com.vention.stock_market_share.repository;

import com.vention.stock_market_share.model.UserFavoriteStock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
@Slf4j
@Repository
public class UserFavoriteStockRepository {
    private final DataSource dataSource;

    public UserFavoriteStockRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    private String SQL_INSERT = "INSERT INTO UserFavoriteStock (user_id, stock_id) VALUES (?, ?)";
    public void save(UserFavoriteStock favoriteStock){
        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            try(PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT)){
                preparedStatement.setLong(1, favoriteStock.getUserId());
                preparedStatement.setLong(2, favoriteStock.getStockId());
                preparedStatement.executeUpdate();
                connection.commit();
            }catch (SQLException e){
                e.printStackTrace();
                connection.rollback();
            }
        } catch (SQLException e) {
            log.error("error occured while saving to the database");
            e.printStackTrace();
        }
    }
}
