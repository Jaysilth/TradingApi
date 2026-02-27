package com.journal.TradingApi.exception.custom;

public class TradeValidationException extends RuntimeException{
    public TradeValidationException(String message){
        super(message);
    }
}

