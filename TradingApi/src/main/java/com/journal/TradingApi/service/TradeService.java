package com.journal.TradingApi.service;

import com.journal.TradingApi.dto.TradeRequestDto;
import com.journal.TradingApi.exception.custom.TradeValidationException;
import com.journal.TradingApi.exception.custom.UserNotFoundException;
import com.journal.TradingApi.model.Trade;
import com.journal.TradingApi.model.TradeDirection;
import com.journal.TradingApi.model.User;
import com.journal.TradingApi.repo.TradeRepo;
import com.journal.TradingApi.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TradeService {

    private final TradeRepo tradeRepo;
    private final UserRepo userRepo;

    // Main method to create a trade
    public Trade createTrade(Long userId, TradeRequestDto dto) {

        // Step 1: Validate incoming DTO
        validateTradeInput(dto);

        // Step 2: Fetch the user safely
        User user = getUserById(userId);

        // Step 3: Map DTO to entity
        Trade trade = mapToEntity(dto, user);

        // Step 4: Validate trading logic (stop loss vs entry)
        validateTradingLogic(trade);

        // Step 5: Calculate profit and risk/reward centrally
        calculateProfitAndRiskReward(trade);

        // Step 6: Save and return trade
        return tradeRepo.save(trade);
    }

    // ---------------- Helper Methods ----------------

    private void validateTradeInput(TradeRequestDto dto) {
        if (dto.getEntryPrice() == null ||
                dto.getStopLoss() == null ||
                dto.getExitPrice() == null ||
                dto.getLotSize() == null ||
                dto.getTradeDirection() == null) {

            throw new TradeValidationException("All trade fields must be provided");
        }
    }

    private User getUserById(Long userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    private Trade mapToEntity(TradeRequestDto dto, User user) {
        Trade trade = new Trade();
        trade.setSymbol(dto.getSymbol());
        trade.setEntryPrice(dto.getEntryPrice());
        trade.setExitPrice(dto.getExitPrice());
        trade.setStopLoss(dto.getStopLoss());
        trade.setLotSize(dto.getLotSize());
        trade.setTradeDirection(dto.getTradeDirection());
        trade.setTradeDate(dto.getTradeDate());
        trade.setNotes(dto.getNotes());
        trade.setUser(user);
        return trade;
    }

    private void validateTradingLogic(Trade trade) {
        if (trade.getTradeDirection() == TradeDirection.BUY &&
                trade.getStopLoss() >= trade.getEntryPrice()) {
            throw new TradeValidationException(
                    "For BUY trades, stop loss must be below entry price");
        }

        if (trade.getTradeDirection() == TradeDirection.SELL &&
                trade.getStopLoss() <= trade.getEntryPrice()) {
            throw new TradeValidationException(
                    "For SELL trades, stop loss must be above entry price");
        }
    }

    private double calculateRisk(Trade trade) {
        if (trade.getTradeDirection() == TradeDirection.BUY) {
            return (trade.getEntryPrice() - trade.getStopLoss()) * trade.getLotSize();
        } else {
            return (trade.getStopLoss() - trade.getEntryPrice()) * trade.getLotSize();
        }
    }

    private double calculateReward(Trade trade) {
        if (trade.getTradeDirection() == TradeDirection.BUY) {
            return (trade.getExitPrice() - trade.getEntryPrice()) * trade.getLotSize();
        } else {
            return (trade.getEntryPrice() - trade.getExitPrice()) * trade.getLotSize();
        }
    }

    // ---------------- Centralized Calculation ----------------
    private void calculateProfitAndRiskReward(Trade trade) {

        double risk = calculateRisk(trade);
        double reward = calculateReward(trade);

        // Prevent division by zero
        if (risk == 0) {
            throw new TradeValidationException("Risk cannot be zero");
        }

        // Round risk/reward ratio for user readability
        double riskReward = Math.round((reward / risk) * 100.0) / 100.0;
        // Store calculated values
        trade.setProfitLoss(reward); // Actual P/L
        trade.setRiskReward(riskReward);
    }

    public List<Trade> getAllTradesForUser(Long userId) {
        User user = getUserById(userId);
        return tradeRepo.findByUser(user);
    }

    public Trade getTradeById(Long tradeId) {
        return tradeRepo.findById(tradeId)
                .orElseThrow(() -> new TradeValidationException("Trade with ID " + tradeId + " not found"));
    }
}