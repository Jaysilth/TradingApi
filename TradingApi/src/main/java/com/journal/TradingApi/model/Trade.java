package com.journal.TradingApi.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String symbol;
    private Double entryPrice;
    private Double exitPrice;
    private Double stopLoss;
    private Double lotSize;

    @Enumerated(EnumType.STRING)
    private TradeDirection tradeDirection; // buy or sell

    private Double profitLoss;// rather than storing lets compute them in the service layer
    private Double riskReward; //calculate them when creating trade, updating trade or fetching analytics
    private LocalDateTime tradeDate;

    @Column(length = 1000)
    private String notes;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;


    public void setProfitLoss(double profitLoss) {
        this.profitLoss = profitLoss;
    }

    public void setRiskReward(double riskReward) {
        this.riskReward = riskReward;
    }
}
