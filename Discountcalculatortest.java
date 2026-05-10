import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DisplayName("DiscountCalculator – Pair-Wise Coverage Tests")
class DiscountCalculatorTest {

    private final DiscountCalculator calculator = new DiscountCalculator();

    
    @ParameterizedTest(name = "[{index}] type={0}, orders={1}, subscribed={2} → {3}%")
    @CsvSource({
       
        "NEW,      5,  true,  7",   
        "NEW,      5,  false, 5",  
        "REGULAR,  5,  true, 10",  
        "REGULAR, 12,  false,13",  
        "PREMIUM,  5,  false,10",  
        "PREMIUM, 12,  true, 15",   
        "REGULAR, 12,  true, 15",   
        "PREMIUM, 12,  false,15"    
    })
    @DisplayName("Pair-Wise discount calculations")
    void testPairWiseCombinations(String customerTypeStr,
                                  int totalOrders,
                                  boolean subscribed,
                                  int expectedDiscount) {

        DiscountCalculator.CustomerType customerType =
                DiscountCalculator.CustomerType.valueOf(customerTypeStr.trim());

        int actual = calculator.calculateDiscount(customerType, totalOrders, subscribed);

        assertEquals(expectedDiscount, actual,
                () -> String.format("Expected %d%% but got %d%% for type=%s, orders=%d, subscribed=%b",
                        expectedDiscount, actual, customerTypeStr, totalOrders, subscribed));
    }

    
    @ParameterizedTest(name = "[{index}] INFEASIBLE – NEW + orders={0}")
    @CsvSource({
        "10",
        "15",
        "100"
    })
    @DisplayName("NEW customer with >= 10 orders should throw IllegalArgumentException")
    void testInfeasibleCombination_NewCustomerHighOrders(int orders) {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculateDiscount(
                        DiscountCalculator.CustomerType.NEW, orders, false),
                "Expected IllegalArgumentException for NEW customer with " + orders + " orders");
    }

 
    @ParameterizedTest(name = "[{index}] Negative orders={0}")
    @CsvSource({"-1", "-5", "-100"})
    @DisplayName("Negative totalOrdersInLastYear should throw IllegalArgumentException")
    void testNegativeOrders(int negativeOrders) {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculateDiscount(
                        DiscountCalculator.CustomerType.REGULAR, negativeOrders, false),
                "Expected IllegalArgumentException for negative order count: " + negativeOrders);
    }
}