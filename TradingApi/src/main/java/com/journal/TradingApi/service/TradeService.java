package com.journal.TradingApi.service;

import com.journal.TradingApi.dto.TradeRequestDto;
import com.journal.TradingApi.dto.TradeUpdateDto;
import com.journal.TradingApi.exception.custom.TradeNotFoundException;
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

    // ================= CREATE TRADE =================
    public Trade createTrade(Long userId, TradeRequestDto dto) {
        validateTradeInput(dto);

        User user = getUserById(userId);
        Trade trade = mapToEntity(dto, user);

        validateTrade(trade);
        recalculateMetrics(trade);

        return tradeRepo.save(trade);
    }

    // ================= UPDATE TRADE =================
    public Trade updateTrade(Long tradeId, TradeUpdateDto dto) {

        Trade trade = getTradeOrThrow(tradeId);

        // Prevent modification if trade is closed
        if (trade.getExitPrice() != null) {
            throw new TradeValidationException("You cannot edit trades after they have been created");
        }

        // Partial updates
        if (dto.getEntryPrice() != null) trade.setEntryPrice(dto.getEntryPrice());
        if (dto.getStopLoss() != null) trade.setStopLoss(dto.getStopLoss());
        if (dto.getLotSize() != null) trade.setLotSize(dto.getLotSize());
        if (dto.getExitPrice() != null) trade.setExitPrice(dto.getExitPrice());

        validateTrade(trade);
        recalculateMetrics(trade);

        return tradeRepo.save(trade);
    }

    // ================= DELETE TRADE =================
    public void deleteTrade(Long tradeId) {
        Trade trade = getTradeOrThrow(tradeId);
        tradeRepo.delete(trade);
    }

    // ================= GET METHODS =================
    public List<Trade> getAllTradesForUser(Long userId) {
        User user = getUserById(userId);
        return tradeRepo.findByUser(user);
    }

    public Trade getTradeById(Long tradeId) {
        return getTradeOrThrow(tradeId);
    }

    // ================= HELPER METHODS =================
    private Trade getTradeOrThrow(Long tradeId) {
        return tradeRepo.findById(tradeId)
                .orElseThrow(() -> new TradeNotFoundException("Trade with ID " + tradeId + " not found"));
    }

    private void validateTradeInput(TradeRequestDto dto) {
        if (dto.getSymbol() == null || dto.getSymbol().isBlank()||
        dto.getEntryPrice() == null ||
                dto.getExitPrice() == null ||
                dto.getStopLoss() == null ||
                dto.getLotSize() == null ||
                dto.getTradeDirection() == null) {

            throw new TradeValidationException(
                    "All trade fields including symbol, entryPrice, exitPrice, stopLoss, lotSize, tradeDirection must be provided"
            );
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

    private void validateTrade(Trade trade) {

        if (trade.getTradeDirection() == TradeDirection.BUY) {
            if (trade.getStopLoss() >= trade.getEntryPrice()) {
                throw new TradeValidationException("Stop loss must be below entry price for BUY trade");
            }
            if (trade.getExitPrice() <= trade.getEntryPrice()) {
                throw new TradeValidationException("Exit price must be above entry price for BUY trade");
            }
        }

        if (trade.getTradeDirection() == TradeDirection.SELL) {
            if (trade.getStopLoss() <= trade.getEntryPrice()) {
                throw new TradeValidationException("Stop loss must be above entry price for SELL trade");
            }
            if (trade.getExitPrice() >= trade.getEntryPrice()) {
                throw new TradeValidationException("Exit price must be below entry price for SELL trade");
            }
        }
    }

    private void recalculateMetrics(Trade trade) {
        double risk = calculateRisk(trade);
        double reward = calculateReward(trade);

        if (risk == 0) throw new TradeValidationException("Risk cannot be zero");

        double riskReward = Math.round((reward / risk) * 100.0) / 100.0;

        trade.setProfitLoss(reward);
        trade.setRiskReward(riskReward);
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
}