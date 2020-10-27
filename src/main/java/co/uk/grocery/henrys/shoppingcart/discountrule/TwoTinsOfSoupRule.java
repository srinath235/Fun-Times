package co.uk.grocery.henrys.shoppingcart.discountrule;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Rule;

@Rule(priority = 1, name = "Buy 2 tins of soup and get a loaf of bread half price")
public class TwoTinsOfSoupRule extends DiscountRule {

    @Condition
    public boolean when() {
        return true;
    }

    @Action
    public Double then() {
        return null;
    }
}
