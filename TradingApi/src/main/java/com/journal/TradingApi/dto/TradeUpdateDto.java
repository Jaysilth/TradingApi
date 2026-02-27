package com.journal.TradingApi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeUpdateDto {

    private Double entryPrice;
    private Double exitPrice;
    private Double stopLoss;
    private Double lotSize;
}
