package co.uk.grocery.henrys.shoppingcart.discountrule;

import co.uk.grocery.henrys.shoppingcart.model.ShoppingBasketRequest;
import co.uk.grocery.henrys.shoppingcart.repository.entity.Discount;

import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.Objects.isNull;

public abstract class DiscountRule {

    protected String ruleName;

    public abstract boolean when(ShoppingBasketRequest shoppingBasketRequest, Iterable<Discount> discounts);

    public abstract void then(ShoppingBasketRequest shoppingBasketRequest);

    protected boolean checkToApplyDiscount(ShoppingBasketRequest shoppingBasketRequest, Iterable<Discount> discounts) {
        boolean shouldApplyDiscount = false;
        if (!isNull(discounts) && !isNull(shoppingBasketRequest)) {
            Spliterator<Discount> discountIterator = discounts.spliterator();
            if (!isNull(discountIterator)) {
                Optional<Discount> optionalDiscount = StreamSupport.stream(discountIterator, false).filter(discount -> discount.getOffer().equals(ruleName)).findFirst();
                if (optionalDiscount.isPresent()) {
                    Discount discount = optionalDiscount.get();
                    if (shoppingBasketRequest.getBoughtDate().after(discount.getValidFrom()) &&
                            shoppingBasketRequest.getBoughtDate().before(discount.getValidTo())) {
                        List<Boolean> productsFound = discount.getDiscountProducts().stream().map(discountProduct ->
                                shoppingBasketRequest.getAddedItems().stream().filter(addedItem -> addedItem.getProductId().equals(discountProduct.getProductId() + "")).findAny().isPresent()
                        ).collect(Collectors.toList());
                        shouldApplyDiscount = !productsFound.contains(false);
                    }
                }
            }
        }
        return shouldApplyDiscount;
    }
}
