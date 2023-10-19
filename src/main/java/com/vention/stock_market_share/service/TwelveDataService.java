package com.vention.stock_market_share.service;

import com.vention.stock_market_share.exception.TwelveDataApiException;
import com.vention.stock_market_share.interfaces.TwelveApiClient;
import com.vention.stock_market_share.response.TwelveDataApiResponse;
import org.springframework.stereotype.Service;

@Service
public class TwelveDataService {
    private final TwelveApiClient twelveApiClient;

    public TwelveDataService(TwelveApiClient twelveApiClient) {
        this.twelveApiClient = twelveApiClient;
    }

    public TwelveDataApiResponse searchStockBySymbol(String symbol) {
        try {
            return twelveApiClient.searchStockBySymbol(symbol);
        } catch (TwelveDataApiException e) {
            throw new TwelveDataApiException("Error while interacting with AlphaVantage API", e);
        }
    }
    public TwelveDataApiResponse searchAllStocks() {
        try {
            final String exchange = "NASDAQ";
            return twelveApiClient.searchAllStocks(exchange);
        } catch (TwelveDataApiException e) {
            throw new TwelveDataApiException("Error while interacting with AlphaVantage API", e);
        }
    }
}
