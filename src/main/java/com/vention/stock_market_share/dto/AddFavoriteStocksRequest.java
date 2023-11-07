package com.vention.stock_market_share.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddFavoriteStocksRequest {
    private Long userId;
    private List<Long> stockIds;
}
