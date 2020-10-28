package co.uk.grocery.henrys.shoppingcart.discountrule;

import co.uk.grocery.henrys.shoppingcart.model.ShoppingBasketRequest;
import co.uk.grocery.henrys.shoppingcart.repository.entity.Discount;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;

import java.math.BigDecimal;

@Rule(priority = 2, name = "Apples have a 10% discount")
public class ApplesRule extends DiscountRule {

    public ApplesRule() {
        ruleName = "Apples have a 10% discount";
    }

    @Override
    @Condition
    public boolean when(@Fact("shoppingBasketRequest") ShoppingBasketRequest shoppingBasketRequest,
                        @Fact("discounts") Iterable<Discount> discounts) {
        return checkToApplyDiscount(shoppingBasketRequest, discounts);
    }

    @Override
    @Action
    public void then(@Fact("shoppingBasketRequest") ShoppingBasketRequest shoppingBasketRequest) {
        double discountCost = shoppingBasketRequest.getAddedItems()
                .stream()
                .filter(addedItem -> addedItem.getProductId().equalsIgnoreCase("4"))
                .map(addedItem -> addedItem.getProductUnitCost()
                        .multiply(new BigDecimal(addedItem.getNumberOfUnits())).multiply(new BigDecimal(0.1)).doubleValue())
                .reduce(0.0, Double::sum);
        double finalCost = shoppingBasketRequest.getTotalAmount() - discountCost;
        shoppingBasketRequest.setTotalAmount(finalCost);
    }
}
