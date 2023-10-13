package com.vention.stock_market_share.exception;

public class MissingEmailException extends RuntimeException{
    public MissingEmailException(String message){
        super(message);
    }

}
