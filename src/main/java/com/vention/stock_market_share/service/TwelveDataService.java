package com.vention.stock_market_share.service;

import com.vention.stock_market_share.exception.TwelveDataApiException;
import com.vention.stock_market_share.interfaces.TwelveApiClient;
import com.vention.stock_market_share.response.TwelveDataApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TwelveDataService {
    private final TwelveApiClient twelveApiClient;

    public TwelveDataApiResponse searchStockBySymbol(String symbol) {
        try {
            return twelveApiClient.searchStockBySymbol(symbol);
        } catch (TwelveDataApiException e) {
            log.error("Error while interacting with TwelveData API");
            return null;
        }
    }

    public TwelveDataApiResponse searchAllStocks() {
        try {
            final String exchange = "NASDAQ";
            return twelveApiClient.searchAllStocks(exchange);
        } catch (TwelveDataApiException e) {
            log.error("Error while interacting with TwelveData API");
            return null;
        }
    }
}
