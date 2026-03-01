package com.journal.TradingApi.repo;

import com.journal.TradingApi.model.Trade;
import com.journal.TradingApi.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeRepo extends JpaRepository<Trade, Long> {

    List<Trade> findByUserId(Long userId);
    Page<Trade> findByUser(User user, Pageable pageable);
}
