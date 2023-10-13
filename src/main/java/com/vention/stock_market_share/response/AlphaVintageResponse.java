package com.vention.stock_market_share.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AlphaVintageResponse {
    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("price")
    private Double price;

    @JsonProperty("volume")
    private Long volume;
}
