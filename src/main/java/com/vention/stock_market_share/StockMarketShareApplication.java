package com.vention.stock_market_share;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.vention.stock_market_share")
@EnableScheduling
public class StockMarketShareApplication {
    public static void main(String[] args) {
        SpringApplication.run(StockMarketShareApplication.class, args);
    }
}

