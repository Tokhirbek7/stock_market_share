package com.vention.stock_market_share.interfaces;

import com.vention.stock_market_share.response.AlphaVintageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Component
@FeignClient(name = "AlphaVintageClient", url = " ${alphavantage.base.url}")
public interface AlphaVintageClient {
    @GetMapping("/query")
    AlphaVintageResponse searchStockBySymbol(@RequestParam("function") String function, @RequestParam("symbol") String symbol, @RequestParam("apikey") String apiKey);

    @GetMapping("/query")
    AlphaVintageResponse searchAllTradingShares(@RequestParam("function") String function, @RequestParam("apikey") String apiKey);
}
