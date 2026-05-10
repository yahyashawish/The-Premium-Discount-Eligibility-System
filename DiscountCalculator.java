
public class DiscountCalculator {

    public enum CustomerType {
        NEW, REGULAR, PREMIUM
    }

   
     
     @param customerType           the type of the customer (NEW, REGULAR, PREMIUM)
     @param totalOrdersInLastYear  the number of orders placed in the last year (must be >= 0)
     @param isSubscribedToNewsletter whether the customer is subscribed to the newsletter
     @return the calculated discount percentage (0–15, inclusive)
     @throws IllegalArgumentException if a NEW customer has 10 or more orders in the last year,
    
    public int calculateDiscount(CustomerType customerType,
                                 int totalOrdersInLastYear,
                                 boolean isSubscribedToNewsletter) {

       
        if (totalOrdersInLastYear < 0) {
            throw new IllegalArgumentException(
                    "totalOrdersInLastYear cannot be negative, but was: " + totalOrdersInLastYear);
        }

        // Constraint / infeasibility guard (Rule 5)
        if (customerType == CustomerType.NEW && totalOrdersInLastYear >= 10) {
            throw new IllegalArgumentException(
                    "A NEW customer cannot have 10 or more orders in the last year. " +
                    "This combination is infeasible.");
        }

        int discount = 0;

        // Rule 1 – base discount
        discount += 5;

        // Rule 2 – newsletter subscription bonus
        if (isSubscribedToNewsletter) {
            discount += 2;
        }

        // Rule 3 – customer type bonus
        switch (customerType) {
            case NEW:
                discount += 0;
                break;
            case REGULAR:
                discount += 3;
                break;
            case PREMIUM:
                discount += 5;
                break;
        }

        // Rule 4 – loyalty bonus
        if (totalOrdersInLastYear >= 10) {
            discount += 5;
        }

        // Rule 6 – cap at 15%
        return Math.min(discount, 15);
    }
}