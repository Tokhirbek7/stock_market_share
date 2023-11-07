package com.vention.stock_market_share.token;

import lombok.Data;

import java.util.Date;

@Data
public class Token {
    private Long id;
    private String body;
    private Date createdAt;
}
