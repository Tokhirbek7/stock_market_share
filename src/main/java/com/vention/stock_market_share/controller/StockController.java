package com.vention.stock_market_share.controller;

import com.vention.stock_market_share.response.AlphaVintageResponse;
import com.vention.stock_market_share.service.AlphaVintageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockController {
    private final AlphaVintageService alphaVintageService;

    @Autowired
    public StockController(AlphaVintageService alphaVintageService) {
        this.alphaVintageService = alphaVintageService;
    }

    @GetMapping("/searchStockBySymbol")
    public ResponseEntity<AlphaVintageResponse> searchStockBySymbol(@RequestParam String symbol, @RequestParam String apiKey) {
        AlphaVintageResponse response = alphaVintageService.searchStockBySymbol(symbol, apiKey);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/searchAllTradingShares")
    public ResponseEntity<AlphaVintageResponse> searchAllTradingShares(@RequestParam String apiKey) {
        AlphaVintageResponse response = alphaVintageService.searchAllTradingShares(apiKey);
        return ResponseEntity.ok(response);
    }
}