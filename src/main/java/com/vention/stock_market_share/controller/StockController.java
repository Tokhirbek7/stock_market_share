package com.vention.stock_market_share.controller;

import com.vention.stock_market_share.dto.AddFavoriteStocksRequest;
import com.vention.stock_market_share.model.Stock;
import com.vention.stock_market_share.response.TwelveDataApiResponse;
import com.vention.stock_market_share.service.TwelveDataService;
import com.vention.stock_market_share.service.UserFavoriteStockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/stocks")
@Slf4j
@RequiredArgsConstructor
public class StockController {
    private final TwelveDataService twelveDataService;
    private final UserFavoriteStockService userFavoriteStockService;

    @GetMapping("/{symbol}")
    public ResponseEntity<TwelveDataApiResponse> searchStockBySymbol(@PathVariable String symbol) {
        TwelveDataApiResponse twelveDataApiResponse = twelveDataService.searchStockBySymbol(symbol);
        return ResponseEntity.ok(twelveDataApiResponse);
    }

    @GetMapping
    public ResponseEntity<?> searchAllStocks() {
        TwelveDataApiResponse twelveDataApiResponse = twelveDataService.searchAllStocks();
        return ResponseEntity.ok(twelveDataApiResponse);
    }

    @GetMapping("/stocks")
    public ResponseEntity<?> getStocksByDateAndSymbol(@RequestParam String symbol, @RequestParam String date) {
        Date parsedDate = userFavoriteStockService.parseDate(date);
        List<Stock> stocksByDateAndSymbol = userFavoriteStockService.getStocksByDateAndSymbol(symbol, parsedDate);
        return (stocksByDateAndSymbol.isEmpty()) ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : ResponseEntity.ok(stocksByDateAndSymbol);
    }

    @PostMapping
    public ResponseEntity<String> addFavoriteStocksForUser(@RequestBody AddFavoriteStocksRequest addFavoriteStocksRequest) {
        try {
            userFavoriteStockService.addFavoriteStocksForUser(addFavoriteStocksRequest);
            return new ResponseEntity<>("Favorite stocks added successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}