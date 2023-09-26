package com.vention.stock_market_share.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecurityInfoDTO {
    private Long id;
    private String username;
    private String password;
    private Long userId;
}
