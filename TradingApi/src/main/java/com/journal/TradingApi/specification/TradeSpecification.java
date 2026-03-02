package com.journal.TradingApi.specification;

import com.journal.TradingApi.model.Trade;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TradeSpecification {

    public static Specification<Trade> filterTrades(
            Long userId,
            String symbol,
            String result,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            // Always filter by user
            predicates.add(
                    criteriaBuilder.equal(root.get("user").get("id"), userId)
            );

            if (symbol != null && !symbol.isBlank()) {
                predicates.add(
                        criteriaBuilder.equal(root.get("symbol"), symbol)
                );
            }

            if (startDate != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("tradeDate"), startDate)
                );
            }

            if (endDate != null) {
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(root.get("tradeDate"), endDate)
                );
            }

            if (result != null) {
                if (result.equalsIgnoreCase("win")) {
                    predicates.add(
                            criteriaBuilder.greaterThan(root.get("profitLoss"), 0)
                    );
                } else if (result.equalsIgnoreCase("loss")) {
                    predicates.add(
                            criteriaBuilder.lessThan(root.get("profitLoss"), 0)
                    );
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}