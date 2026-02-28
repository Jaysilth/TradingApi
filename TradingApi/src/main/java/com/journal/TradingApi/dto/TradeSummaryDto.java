package com.journal.TradingApi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TradeSummaryDto {

    private long totalTrades;
    private long wins;
    private long losses;
    private double winRate;
    private double netProfit;
}