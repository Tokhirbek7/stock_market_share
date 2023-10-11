package com.vention.stock_market_share.service;

import com.vention.stock_market_share.interfaces.AlphaVintageClient;
import com.vention.stock_market_share.response.AlphaVintageResponse;
import org.springframework.stereotype.Service;

@Service
public class AlphaVintageService {
    private final AlphaVintageClient alphaVintageClient;

    public AlphaVintageService(AlphaVintageClient alphaVintageClient) {
        this.alphaVintageClient = alphaVintageClient;
    }
    public AlphaVintageResponse searchStockBySymbol(String symbol, String apiKey) {
        return alphaVintageClient.searchStockBySymbol("TIME_SERIES_INTRADAY", symbol, apiKey);
    }

    public AlphaVintageResponse searchAllTradingShares(String apiKey) {
        return alphaVintageClient.searchAllTradingShares("TIME_SERIES_INTRADAY", apiKey);
    }

}
