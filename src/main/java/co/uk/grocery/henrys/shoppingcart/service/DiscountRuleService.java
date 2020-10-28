package co.uk.grocery.henrys.shoppingcart.service;

import co.uk.grocery.henrys.shoppingcart.model.ShoppingBasketRequest;
import co.uk.grocery.henrys.shoppingcart.repository.DiscountRepository;
import lombok.RequiredArgsConstructor;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class DiscountRuleService {

    private final Rules rules;
    private final RulesEngine rulesEngine;
    private final DiscountRepository discountRepository;

    public BigDecimal applyDiscount(ShoppingBasketRequest shoppingBasketRequest) {
        double totalCost = 0;
        if (!isNull(shoppingBasketRequest) && !isNull(shoppingBasketRequest.getAddedItems())) {
            totalCost = shoppingBasketRequest.getAddedItems()
                    .stream()
                    .map(addedItem -> addedItem.getProductUnitCost()
                            .multiply(new BigDecimal(addedItem.getNumberOfUnits())).doubleValue())
                    .reduce(0.0, Double::sum);
            shoppingBasketRequest.setTotalAmount(totalCost);
            Facts facts = new Facts();
            facts.put("shoppingBasketRequest", shoppingBasketRequest);
            facts.put("discounts", discountRepository.findAll());

            rulesEngine.fire(rules, facts);
            totalCost = ((ShoppingBasketRequest) facts.getFact("shoppingBasketRequest").getValue()).getTotalAmount();
        }
        return new BigDecimal(totalCost).setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }
}
