package com.vention.stock_market_share.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    private String firstname;
    private String lastname;
    private String email;
    private int age;
}
