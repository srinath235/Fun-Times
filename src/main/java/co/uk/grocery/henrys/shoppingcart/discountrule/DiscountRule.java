package co.uk.grocery.henrys.shoppingcart.discountrule;

import co.uk.grocery.henrys.shoppingcart.model.ShoppingBasketRequest;
import org.jeasy.rules.core.BasicRule;

public abstract class DiscountRule extends BasicRule {
    protected ShoppingBasketRequest shoppingBasketRequest;
    public void setShoppingBasketRequest(ShoppingBasketRequest shoppingBasketRequest) {
        this.shoppingBasketRequest = shoppingBasketRequest;
    }
}
