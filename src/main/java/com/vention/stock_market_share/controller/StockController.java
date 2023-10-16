package com.vention.stock_market_share.controller;

import com.vention.stock_market_share.response.AlphaVintageResponse;
import com.vention.stock_market_share.service.AlphaVintageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/stock")
public class StockController {
    private final AlphaVintageService alphaVintageService;

    @Autowired
    public StockController(AlphaVintageService alphaVintageService) {
        this.alphaVintageService = alphaVintageService;
    }

    @GetMapping("/{symbol}")
    public ResponseEntity<AlphaVintageResponse> searchStockBySymbol(@PathVariable String symbol, @Value("${alphavantage.api.key}") String apiKey) {
        AlphaVintageResponse response = alphaVintageService.searchStockBySymbol(symbol, apiKey);
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<AlphaVintageResponse> searchAllTradingShares(@Value("${alphavantage.api.key}") String apiKey) {
        AlphaVintageResponse response = alphaVintageService.searchAllTradingShares(apiKey);
        return ResponseEntity.ok(response);

    }
}