package com.journal.TradingApi.dto;

import com.journal.TradingApi.model.TradeDirection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeRequestDto {

    private String symbol;           // e.g., "EURUSD"
    private Double entryPrice;       // entry price of the trade
    private Double exitPrice;        // exit price of the trade
    private Double stopLoss;         // stop loss price
    private Double lotSize;          // size of the trade
    private TradeDirection tradeDirection; // BUY or SELL
    private LocalDateTime tradeDate; // date & time of trade
    private String notes;            // optional notes
}