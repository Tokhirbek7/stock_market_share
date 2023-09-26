package com.vention.stock_market_share.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private int age;
    private SecurityInfo securityInfo;

}
