package com.vention.stock_market_share;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.vention.stock_market_share")
public class StockMarketShareApplication {
    public static void main(String[] args) {
        SpringApplication.run(StockMarketShareApplication.class, args);
    }
}

