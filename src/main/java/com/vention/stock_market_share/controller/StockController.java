package com.vention.stock_market_share.controller;

import com.vention.stock_market_share.response.TwelveDataApiResponse;
import com.vention.stock_market_share.service.TwelveDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stocks")
@Slf4j
public class StockController {
    private final TwelveDataService twelveDataService;
    public StockController(TwelveDataService twelveDataService) {
        this.twelveDataService = twelveDataService;
    }

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
}