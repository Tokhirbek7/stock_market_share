package com.vention.stock_market_share.exception;

public class DuplicateUsernameException extends RuntimeException {
    public DuplicateUsernameException(String message){
        super(message);
    }

}
