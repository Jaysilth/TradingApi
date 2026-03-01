package com.journal.TradingApi.dto;

import com.journal.TradingApi.model.TradeDirection;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TradeResponseDto {

    private Long id;
    private String symbol;
    private Double entryPrice;
    private Double exitPrice;
    private Double stopLoss;
    private Double lotSize;
    private TradeDirection tradeDirection;
    private Double profitLoss;
    private Double riskReward;
    private LocalDateTime tradeDate;
    private String notes;
}