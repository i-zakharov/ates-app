package ru.zim.ates.billing.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;
import lombok.Getter;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;

@Service
public class PricingService {

    public TaskPrices getPrices() {

        BigDecimal assignePrice = getRandomPrice(-20., -10.);
        BigDecimal closePrice = getRandomPrice(20., 40.);;
        return new TaskPrices(assignePrice, closePrice);
    }

    private BigDecimal getRandomPrice(Double lowerBound, Double upperBound) {
        BigDecimal value = BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(lowerBound, upperBound));
        return value.setScale(2, RoundingMode.HALF_UP);
    }

    @Getter
    public static class TaskPrices {
        private final BigDecimal assignePrice;
        private final BigDecimal closePrice;

        public TaskPrices(BigDecimal assignePrice, BigDecimal closePrice) {
            this.assignePrice = assignePrice;
            this.closePrice = closePrice;
        }
    }
}
