package com.vention.stock_market_share.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private int age;



}
