package com.vention.stock_market_share.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserFavoriteStock {
    private Long id;
    private Long userId;
    private Long stockId;

    public UserFavoriteStock(Long userId, Long stockId) {
        this.userId = userId;
        this.stockId = stockId;
    }
}
