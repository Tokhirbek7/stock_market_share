package com.vention.stock_market_share.model;

import com.vention.stock_market_share.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private int age;
    private Role role;
    private boolean isVerified;
}
