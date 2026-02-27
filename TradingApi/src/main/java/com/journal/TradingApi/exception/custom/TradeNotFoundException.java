package com.journal.TradingApi.exception.custom;

public class TradeNotFoundException extends RuntimeException {

    public TradeNotFoundException(String message) {
        super(message);
    }
}
