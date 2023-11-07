package com.vention.stock_market_share.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SecurityInfo {
    private Long id;
    private String username;
    private String password;
    private Long userId;
}
