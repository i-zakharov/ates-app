package ru.zim.ates.billing;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.zim.ates.billing.service.PricingService;
import static org.junit.jupiter.api.Assertions.*;

public class PricingServiceTest {

    static PricingService pricingService;

    @BeforeAll
    public static void init() {
        pricingService = new PricingService();
    }

    @Test
    public void test() {
        for (int i = 0; i < 100; i++) {
            PricingService.TaskPrices prices = pricingService.getPrices();
            System.out.println(prices.getAssignePrice().toString() + " " + prices.getClosePrice().toString());
            assertTrue(prices.getAssignePrice().compareTo(BigDecimal.valueOf(-20.)) >= 0);
            assertTrue(prices.getAssignePrice().compareTo(BigDecimal.valueOf(-10.)) <= 0);
            assertTrue(prices.getClosePrice().compareTo(BigDecimal.valueOf(20.)) >= 0);
            assertTrue(prices.getAssignePrice().compareTo(BigDecimal.valueOf(40.)) <= 0);
            assertEquals(prices.getAssignePrice().scale(), 2);
            assertEquals(prices.getClosePrice().scale(), 2);
        }
    }
}
