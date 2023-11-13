package com.vention.stock_market_share.service;

import com.vention.stock_market_share.exception.DataNotFoundException;
import com.vention.stock_market_share.model.Stock;
import com.vention.stock_market_share.repository.StockDataRepository;
import com.vention.stock_market_share.response.TwelveDataApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DailyStockUpdaterService {
    private final TwelveDataService twelveDataService;
    private final StockDataRepository stockDataRepository;

    @Scheduled(cron = "0 4 12 * * *")
    public void dailyUpdateAllStockData() {
        TwelveDataApiResponse twelveDataApiResponse = twelveDataService.searchAllStocks();
        List<TwelveDataApiResponse.StockData> data = twelveDataApiResponse.getData();
        List<Stock> stockList = new ArrayList<>();
        for (TwelveDataApiResponse.StockData datum : data) {
            Stock stock = new Stock();
            stock.setSymbol(datum.getSymbol());
            stock.setName(datum.getName());
            stock.setCountry(datum.getCountry());
            stock.setCurrency(datum.getCurrency());
            stock.setExchange(datum.getExchange());
            stock.setMicCode(datum.getMicCode());
            stock.setCountry(datum.getCountry());
            stock.setType(datum.getType());
            stock.setDate(new Date());
            stockList.add(stock);
        }
        for (Stock value : stockList) {
            stockDataRepository.save(value);
        }
    }

    public List<Stock> getStocksByDateAndSymbol(String symbol, Date date) {
        List<Stock> stocksByDateAndSymbol = stockDataRepository.getStocksByDateAndSymbol(symbol, date);
        if (stocksByDateAndSymbol.isEmpty()) {
            throw new DataNotFoundException("No data found with this symbol: " + symbol + " and date: " + date);
        }
        return stocksByDateAndSymbol;
    }
}
