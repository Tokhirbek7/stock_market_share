package com.vention.stock_market_share.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TwelveDataApiResponse {
    @JsonProperty("data")
    private List<StockData> data;
    @JsonProperty("status")
    private String status;

    @Data
    public static class StockData {
        @JsonProperty("symbol")
        private String symbol;

        @JsonProperty("name")
        private String name;

        @JsonProperty("currency")
        private String currency;

        @JsonProperty("exchange")
        private String exchange;

        @JsonProperty("mic_code")
        private String micCode;

        @JsonProperty("country")
        private String country;

        @JsonProperty("type")
        private String type;
    }
}
