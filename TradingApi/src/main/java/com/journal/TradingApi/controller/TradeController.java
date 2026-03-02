package com.journal.TradingApi.controller;

import com.journal.TradingApi.dto.TradeRequestDto;
import com.journal.TradingApi.dto.TradeResponseDto;
import com.journal.TradingApi.dto.TradeSummaryDto;
import com.journal.TradingApi.model.Trade;
import com.journal.TradingApi.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/trades")
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;

    @PostMapping("/user/{userId}")
    public ResponseEntity<Trade> createTrade(@PathVariable Long userId,
                                             @RequestBody TradeRequestDto dto) {
        Trade trade = tradeService.createTrade(userId, dto);
        return new ResponseEntity<>(trade, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<TradeResponseDto>> getAllTradesForUser(
            @PathVariable Long userId,
            @RequestParam(required = false) String symbol,
            @RequestParam(required = false) String result,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "tradeDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {

        Page<TradeResponseDto> trades = tradeService.getAllTradesForUser(
                userId,
                symbol,
                result,
                startDate,
                endDate,
                page,
                size,
                sortBy,
                direction
        );

        return ResponseEntity.ok(trades);
    }
    @GetMapping("/{tradeId}")
    public ResponseEntity<Trade> getTradeById(@PathVariable Long tradeId) {
        Trade trade = tradeService.getTradeById(tradeId);
        return ResponseEntity.ok(trade);
    }

    @GetMapping("/user/{userId}/summary")
    public ResponseEntity<TradeSummaryDto> getTradeSummary(@PathVariable Long userId) {
        TradeSummaryDto summary = tradeService.getTradeSummary(userId);
        return ResponseEntity.ok(summary);
    }

    @DeleteMapping("/{tradeId}")
    public ResponseEntity<String> deleteTrade(@PathVariable Long tradeId) {
        tradeService.deleteTrade(tradeId);
        return ResponseEntity.ok("Trade deleted successfully");
    }

    @PutMapping("/{tradeId}")
    public ResponseEntity<String> updateTrade(@PathVariable Long tradeId,
                                              @RequestBody TradeRequestDto dto) {
        // Disabled: all trades are closed by default
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body("You can't edit your trade values right now, delete and create a new trade instead");
    }
}