package com.vention.stock_market_share.model;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SecurityInfo {

    private Long id;
    private String username;
    private String password;
    private Long userId;
    private User user;



}
