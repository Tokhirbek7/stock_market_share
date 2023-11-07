package com.vention.stock_market_share.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Stock {
    private Long id;
    private String symbol;
    private String name;
    private String currency;
    private String exchange;
    private String micCode;
    private String country;
    private String type;
    private Date date;
}
