package com.vention.stock_market_share.controller;

import com.vention.stock_market_share.service.UserFavoriteStockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/favourite-stocks")
public class UserFavouriteStockController {

    private final UserFavoriteStockService userFavoriteStockService;

    public UserFavouriteStockController(UserFavoriteStockService userFavoriteStockService) {
        this.userFavoriteStockService = userFavoriteStockService;
    }

    @PostMapping
    public ResponseEntity<String> addFavoriteStocksForUser(@RequestBody Long userId, @RequestBody List<Long> stockIds) {
        try {
            userFavoriteStockService.addFavoriteStocksForUser(userId, stockIds);
            return new ResponseEntity<>("Favorite stocks added successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add favorite stocks: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
