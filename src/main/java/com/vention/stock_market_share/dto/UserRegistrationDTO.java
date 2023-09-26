package com.vention.stock_market_share.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationDTO {
    private String firstname;
    private String lastname;
    private String email;
    private int age;
    private String username;
    private String password;


}
