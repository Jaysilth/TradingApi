package com.journal.TradingApi.service;

import com.journal.TradingApi.model.Trade;
import com.journal.TradingApi.repo.TradeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TradeAnalyticsService {

    private final TradeRepo tradeRepo;

    public double calculateAverageWin(Long userId){
        List<Trade> trades = tradeRepo.findByUserId(userId);

        double totalWins = 0;
        int winCount = 0;

        for (Trade trade: trades){
            double profit =  calculateReward(trade);

            if (profit>0){
                totalWins += profit;
                winCount++;
            }
        }
        return winCount == 0 ? 0 : totalWins/winCount;
    }
}
