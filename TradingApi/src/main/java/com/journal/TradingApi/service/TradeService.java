package com.journal.TradingApi.service;

import com.journal.TradingApi.dto.TradeRequestDto;
import com.journal.TradingApi.dto.TradeResponseDto;
import com.journal.TradingApi.dto.TradeSummaryDto;
import com.journal.TradingApi.dto.TradeUpdateDto;
import com.journal.TradingApi.exception.custom.TradeNotFoundException;
import com.journal.TradingApi.exception.custom.TradeValidationException;
import com.journal.TradingApi.exception.custom.UserNotFoundException;
import com.journal.TradingApi.model.Trade;
import com.journal.TradingApi.model.TradeDirection;
import com.journal.TradingApi.model.User;
import com.journal.TradingApi.repo.TradeRepo;
import com.journal.TradingApi.repo.UserRepo;
import com.journal.TradingApi.specification.TradeSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

        validateTrade(trade); // ✅ validation now allows losses
        recalculateMetrics(trade); // profit/loss dynamically calculated

        return tradeRepo.save(trade);
    }

    // ================= UPDATE TRADE =================
    public Trade updateTrade(Long tradeId, TradeUpdateDto dto) {
        Trade trade = getTradeOrThrow(tradeId);

        // Partial updates
        if (dto.getEntryPrice() != null) trade.setEntryPrice(dto.getEntryPrice());
        if (dto.getStopLoss() != null) trade.setStopLoss(dto.getStopLoss());
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
    public Page<TradeResponseDto> getAllTradesForUser(
            Long userId,
            String symbol,
            String result,
            LocalDateTime startDate,
            LocalDateTime endDate,
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        int MAX_PAGE_SIZE = 50;// 50 is the max no of trades that can be returned in one request

        if (size > MAX_PAGE_SIZE) size = MAX_PAGE_SIZE;// forces the max page size constraint
        if (size <= 0) size = 10;

        List<String> allowedSortFields = List.of("tradeDate", "profitLoss", "symbol");

        if (!allowedSortFields.contains(sortBy)) {
            sortBy = "tradeDate";
        }

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Trade> spec = TradeSpecification.filterTrades(
                userId,
                symbol,
                result,
                startDate,
                endDate
        );

        Page<Trade> tradePage = tradeRepo.findAll(spec, pageable);

        return tradePage.map(this::mapToResponseDto);
    }

    public Trade getTradeById(Long tradeId) {
        return getTradeOrThrow(tradeId);
    }

    // ================= TRADE SUMMARY =================
    public TradeSummaryDto getTradeSummary(Long userId) {
        List<Trade> trades = tradeRepo.findByUserId(userId);

        long totalTrades = 0;
        long wins = 0;
        long losses = 0;
        double netProfit = 0;

        for (Trade trade : trades) {
            totalTrades++;
            double profit = calculateReward(trade);
            netProfit += profit;

            if (profit > 0) wins++;
            else if (profit < 0) losses++;
        }

        double winRate = totalTrades == 0 ? 0 : ((double) wins / totalTrades) * 100;
        winRate = Math.round(winRate * 100.0) / 100.0;
        netProfit = Math.round(netProfit * 100.0) / 100.0;

        return new TradeSummaryDto(totalTrades, wins, losses, winRate, netProfit);
    }

    // ================= HELPER METHODS =================
    private Trade getTradeOrThrow(Long tradeId) {
        return tradeRepo.findById(tradeId)
                .orElseThrow(() -> new TradeNotFoundException("Trade with ID " + tradeId + " not found"));
    }

    private void validateTradeInput(TradeRequestDto dto) {if (dto.getSymbol() == null || dto.getSymbol().isBlank()
            || dto.getEntryPrice() == null
            || dto.getExitPrice() == null
            || dto.getStopLoss() == null
            || dto.getLotSize() == null
            || dto.getTradeDirection() == null) {

        throw new TradeValidationException(
                "All trade fields including symbol, entryPrice, exitPrice, stopLoss, lotSize, tradeDirection must be provided"
        );
    }
    }

    private TradeResponseDto mapToResponseDto(Trade trade) {
        return new TradeResponseDto(
                trade.getId(),
                trade.getSymbol(),
                trade.getEntryPrice(),
                trade.getExitPrice(),
                trade.getStopLoss(),
                trade.getLotSize(),
                trade.getTradeDirection(),
                trade.getProfitLoss(),
                trade.getRiskReward(),
                trade.getTradeDate(),
                trade.getNotes()
        );
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

    // ================= DOMAIN VALIDATION =================
    private void validateTrade(Trade trade) {
        // ✅ only enforce stop loss direction
        if (trade.getTradeDirection() == TradeDirection.BUY && trade.getStopLoss() >= trade.getEntryPrice()) {
            throw new TradeValidationException("Stop loss must be below entry price for BUY trade");
        }

        if (trade.getTradeDirection() == TradeDirection.SELL && trade.getStopLoss() <= trade.getEntryPrice()) {
            throw new TradeValidationException("Stop loss must be above entry price for SELL trade");
        }

        if (trade.getLotSize() <= 0) {
            throw new TradeValidationException("Lot size must be positive");
        }

    }

    private void recalculateMetrics(Trade trade) {
        double risk = calculateRisk(trade);
        double reward = calculateReward(trade);

        if (risk == 0) throw new TradeValidationException("Risk cannot be zero");

        double riskReward = Math.round((reward / risk) * 100.0) / 100.0;

        trade.setProfitLoss(reward); // can be negative
        trade.setRiskReward(riskReward);
    }

    private double calculateRisk(Trade trade) {
        return Math.abs(trade.getEntryPrice() - trade.getStopLoss()) * trade.getLotSize();
    }

    private double calculateReward(Trade trade) {
        return (trade.getTradeDirection() == TradeDirection.BUY)
                ? (trade.getExitPrice() - trade.getEntryPrice()) * trade.getLotSize()
                : (trade.getEntryPrice() - trade.getExitPrice()) * trade.getLotSize();
    }

}