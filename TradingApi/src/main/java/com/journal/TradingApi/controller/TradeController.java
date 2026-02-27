package com.journal.TradingApi.controller;

import com.journal.TradingApi.dto.TradeRequestDto;
import com.journal.TradingApi.dto.TradeUpdateDto;
import com.journal.TradingApi.model.Trade;
import com.journal.TradingApi.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/trades")
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;

    // 1️⃣ Create a trade for a user
    @PostMapping("/user/{userId}")
    public ResponseEntity<Trade> createTrade(@PathVariable Long userId,
                                             @RequestBody TradeRequestDto dto) {
        Trade trade = tradeService.createTrade(userId, dto);
        return new ResponseEntity<>(trade, HttpStatus.CREATED);
    }

    // 2️⃣ Get all trades for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Trade>> getAllTradesForUser(@PathVariable Long userId) {
        List<Trade> trades = tradeService.getAllTradesForUser(userId);
        return new ResponseEntity<>(trades, HttpStatus.OK);
    }

    // 3️⃣ Get a single trade by its ID
    @GetMapping("/{tradeId}")
    public ResponseEntity<Trade> getTradeById(@PathVariable Long tradeId) {
        Trade trade = tradeService.getTradeById(tradeId);
        return new ResponseEntity<>(trade, HttpStatus.OK);
    }

    // 4️⃣ Update a trade
    @PutMapping("/{tradeId}")
    public ResponseEntity<Trade> updateTrade(@PathVariable Long tradeId,
                                             @RequestBody TradeUpdateDto dto) {
        Trade updatedTrade = tradeService.updateTrade(tradeId, dto);
        return ResponseEntity.ok(updatedTrade);
    }

    // 5️⃣ Delete a trade
    @DeleteMapping("/{tradeId}")
    public ResponseEntity<String> deleteTrade(@PathVariable Long tradeId) {
        tradeService.deleteTrade(tradeId);
        return ResponseEntity.ok("Trade deleted successfully");
    }
}