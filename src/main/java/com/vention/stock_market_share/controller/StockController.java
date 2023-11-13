package com.vention.stock_market_share.controller;

import com.vention.stock_market_share.dto.AddFavoriteStocksRequest;
import com.vention.stock_market_share.model.Stock;
import com.vention.stock_market_share.response.TwelveDataApiResponse;
import com.vention.stock_market_share.service.AuthenticationService;
import com.vention.stock_market_share.service.DailyStockUpdaterService;
import com.vention.stock_market_share.service.TwelveDataService;
import com.vention.stock_market_share.service.UserFavoriteStockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/stocks")
@Slf4j
@RequiredArgsConstructor
public class StockController {
    private final TwelveDataService twelveDataService;
    private final DailyStockUpdaterService dailyStockUpdaterService;
    private final UserFavoriteStockService userFavoriteStockService;
    private final AuthenticationService authenticationService;

    @GetMapping("/{symbol}")
    public ResponseEntity<?> searchStockBySymbol(@PathVariable String symbol) {
        TwelveDataApiResponse twelveDataApiResponse = twelveDataService.searchStockBySymbol(symbol);
        if (twelveDataApiResponse.getData().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("stocks with this symbol: " + symbol + " are not found");
        }
        return ResponseEntity.ok(twelveDataApiResponse);
    }

    @GetMapping
    public ResponseEntity<?> searchAllStocks() {
        TwelveDataApiResponse twelveDataApiResponse = twelveDataService.searchAllStocks();
        if (twelveDataApiResponse.getData().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(twelveDataApiResponse);
    }

    @GetMapping("/{symbol}/{date}")
    public ResponseEntity<?> getStocksByDateAndSymbol(@PathVariable String symbol, @PathVariable String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedDate = dateFormat.parse(date);
        List<Stock> stocksByDateAndSymbol = dailyStockUpdaterService.getStocksByDateAndSymbol(symbol, parsedDate);
        return (stocksByDateAndSymbol.isEmpty()) ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : ResponseEntity.ok(stocksByDateAndSymbol);
    }

    @PostMapping
    public ResponseEntity<?> addFavoriteStocksForUser(@RequestBody AddFavoriteStocksRequest addFavoriteStocksRequest) {
        if (Objects.equals(authenticationService.getCurrentUserId(), addFavoriteStocksRequest.getUserId())) {
            boolean saved = userFavoriteStockService.addFavoriteStocksForUser(addFavoriteStocksRequest);
            if (saved) {
                return new ResponseEntity<>("Favorite stocks added successfully", HttpStatus.CREATED);
            }
            return new ResponseEntity<>("Favorite stocks already added", HttpStatus.OK);
        }
        return new ResponseEntity<>("UnAuthorized user", HttpStatus.UNAUTHORIZED);
    }
}