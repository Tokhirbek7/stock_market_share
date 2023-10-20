package com.vention.stock_market_share.service;

import com.vention.stock_market_share.model.Stock;
import com.vention.stock_market_share.model.User;
import com.vention.stock_market_share.model.UserFavoriteStock;
import com.vention.stock_market_share.repository.StockDataRepository;
import com.vention.stock_market_share.repository.UserFavoriteStockRepository;
import com.vention.stock_market_share.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserFavoriteStockService {
    private final UserFavoriteStockRepository userFavoriteStockRepository;
    private final UserRepository userRepository;
    private final StockDataRepository stockDataRepository;

    public UserFavoriteStockService(UserFavoriteStockRepository userFavoriteStockRepository, UserRepository userRepository, StockDataRepository stockDataRepository) {
        this.userFavoriteStockRepository = userFavoriteStockRepository;
        this.userRepository = userRepository;
        this.stockDataRepository = stockDataRepository;
    }

    public void addFavoriteStocksForUser(Long userId, List<Long> stockIds) {
        if (!userExists(userId) || !allStocksExist(stockIds)) {
            throw new IllegalArgumentException("User or stock not found");
        }
        for (Long stockId : stockIds) {
            UserFavoriteStock favoriteStock = new UserFavoriteStock(userId, stockId);
            userFavoriteStockRepository.save(favoriteStock);
        }
    }

    private boolean allStocksExist(List<Long> stockIds) {
        List<Stock> stockList = new ArrayList<>();
        for (Long stockId : stockIds) {
            Stock stockById = stockDataRepository.findStockById(stockId);
            stockList.add(stockById);
        }
        return !stockList.isEmpty();
    }

    private boolean userExists(Long userId) {
        User byId = userRepository.findById(userId);
        return byId != null;
    }
}
