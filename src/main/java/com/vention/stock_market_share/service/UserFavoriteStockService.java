package com.vention.stock_market_share.service;

import com.vention.stock_market_share.dto.AddFavoriteStocksRequest;
import com.vention.stock_market_share.model.Stock;
import com.vention.stock_market_share.model.User;
import com.vention.stock_market_share.model.UserFavoriteStock;
import com.vention.stock_market_share.repository.StockDataRepository;
import com.vention.stock_market_share.repository.UserFavoriteStockRepository;
import com.vention.stock_market_share.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserFavoriteStockService {
    private final UserFavoriteStockRepository userFavoriteStockRepository;
    private final UserRepository userRepository;
    private final StockDataRepository stockDataRepository;

    public void addFavoriteStocksForUser(AddFavoriteStocksRequest addFavoriteStocksRequest) {
        if (!userExists(addFavoriteStocksRequest.getUserId()) || !allStocksExist(addFavoriteStocksRequest.getStockIds())) {
            throw new IllegalArgumentException("User or stock not found");
        }
        boolean isExisted = true;
        for (Long stockId : addFavoriteStocksRequest.getStockIds()) {
            if (!userFavoriteStockRepository.existsByUserIdAndStockId(addFavoriteStocksRequest.getUserId(), stockId)) {
                isExisted = false;
                UserFavoriteStock favoriteStock = new UserFavoriteStock(addFavoriteStocksRequest.getUserId(), stockId);
                userFavoriteStockRepository.save(favoriteStock);
            }
        }
        if (isExisted) {
            throw new IllegalArgumentException("This stock has already been added to your favourites!!");
        }
    }

    public List<Stock> getFavoriteStocks(Long userId) {
        return userFavoriteStockRepository.getFavoriteStocks(userId);
    }

    public List<User> getAllUsersWhoLikedStock() {
        return userFavoriteStockRepository.getAllUsersWhoLikedStocks();
    }

    public List<Stock> getStocksByDateAndSymbol(String symbol, Date date) {
        return stockDataRepository.getStocksByDateAndSymbol(symbol, date);
    }

    private boolean allStocksExist(List<Long> stockIds) {
        List<Stock> stockList = new ArrayList<>();
        for (Long stockId : stockIds) {
            Optional<Stock> stockById = stockDataRepository.findStockById(stockId);
            if (stockById.isEmpty()) {
                return false;
            }
            stockList.add(stockById.get());
        }
        return !stockList.isEmpty();
    }

    private boolean userExists(Long userId) {
        User byId = userRepository.findById(userId);
        return byId != null;
    }

    public Date parseDate(String date) {
        try {
            LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
            return java.sql.Date.valueOf(localDate);
        } catch (Exception e) {
            log.error("Error parsing date: " + date, e);
            return null;
        }
    }
}
