package co.uk.grocery.henrys.shoppingcart.discountrule;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Rule;

@Rule(priority = 2, name = "Apples have a 10% discount")
public class ApplesRule extends DiscountRule {
    @Condition
    public boolean when() {
        return true;
    }

    @Action
    public Double then() {
        return null;
    }
}
