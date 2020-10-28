package co.uk.grocery.henrys.shoppingcart.discountrule;

import co.uk.grocery.henrys.shoppingcart.model.ShoppingBasketRequest;
import co.uk.grocery.henrys.shoppingcart.repository.entity.Discount;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;

@Rule(priority = 1, name = "Buy 2 tins of soup and get a loaf of bread half price")
public class TwoTinsOfSoupRule extends DiscountRule {

    public TwoTinsOfSoupRule() {
        ruleName = "Buy 2 tins of soup and get a loaf of bread half price";
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
        int totalNumberOfSoupTins = shoppingBasketRequest.getAddedItems()
                .stream()
                .map(addedItem -> addedItem.getProductId().equalsIgnoreCase("1") ? addedItem.getNumberOfUnits() : 0)
                .reduce(0, Integer::sum);
        int tinFactor = totalNumberOfSoupTins / 2;
        int totalNumberOfBreadLoafs = shoppingBasketRequest.getAddedItems()
                .stream()
                .map(addedItem -> addedItem.getProductId().equalsIgnoreCase("2") ? addedItem.getNumberOfUnits() : 0)
                .reduce(0, Integer::sum);
        double unitCost = shoppingBasketRequest.getAddedItems()
                .stream().filter(addedItem -> addedItem.getProductId().equalsIgnoreCase("2")).findFirst().get().getProductUnitCost().doubleValue();
        double totalCostForBreadLoafs = unitCost * totalNumberOfBreadLoafs;
        double discountCost = unitCost * 0.5 * tinFactor;
        if (totalNumberOfBreadLoafs < tinFactor && (totalCostForBreadLoafs - discountCost) < 0) {
            discountCost = totalCostForBreadLoafs;
        }
        double finalCost = shoppingBasketRequest.getTotalAmount() - discountCost;
        shoppingBasketRequest.setTotalAmount(finalCost);
    }
}
