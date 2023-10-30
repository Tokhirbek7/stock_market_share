package com.vention.stock_market_share.interfaces;

import com.vention.stock_market_share.response.TwelveDataApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "twelveApi", url = "${twelveDate.base.url}")

public interface TwelveApiClient {
    @GetMapping("/stocks")
    TwelveDataApiResponse searchStockBySymbol(@RequestParam("symbol") String symbol);
    @GetMapping("/stocks")
    TwelveDataApiResponse searchAllStocks(@RequestParam("exchange") String exchange);
}

