package co.uk.grocery.henrys.shoppingcart.service;

import co.uk.grocery.henrys.shoppingcart.model.AddedItem;
import co.uk.grocery.henrys.shoppingcart.model.ShoppingBasketRequest;
import co.uk.grocery.henrys.shoppingcart.repository.DiscountRepository;
import co.uk.grocery.henrys.shoppingcart.repository.entity.Discount;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DiscountRuleServiceTest {

    private DiscountRuleService discountRuleService;

    private Rules rules;
    private RulesEngine rulesEngine;
    private DiscountRepository discountRepository;
    private Iterable<Discount> discounts;

    @BeforeEach
    void setup() {
        rules = mock(Rules.class);
        rulesEngine = mock(RulesEngine.class);
        discountRepository = mock(DiscountRepository.class);
        discounts = mock(Iterable.class);

        when(discountRepository.findAll()).thenReturn(discounts);
        this.discountRuleService = new DiscountRuleService(rules, rulesEngine, discountRepository);
    }

    @Test
    void applyDiscount_whenShoppingIsNull_shouldReturn0() {
        BigDecimal actual = discountRuleService.applyDiscount(null);
        assertEquals(0, actual.doubleValue());
    }

    @Test
    void applyDiscount_whenShoppingAddedItemsIsNull_shouldReturn0() {
        BigDecimal actual = discountRuleService.applyDiscount(new ShoppingBasketRequest());
        assertEquals(0, actual.doubleValue());
    }

    @Test
    void applyDiscount_whenShoppingAddedItemsIsEmpty_shouldReturn0() {
        ShoppingBasketRequest shoppingBasketRequest = ShoppingBasketRequest.builder().addedItems(new ArrayList<>()).build();
        BigDecimal actual = discountRuleService.applyDiscount(shoppingBasketRequest);
        assertEquals(0, actual.doubleValue());
    }

    @Test
    void applyDiscount_whenShoppingAddedItems_shouldInvokeRulesEngineToApplyRules() {
        ShoppingBasketRequest shoppingBasketRequest = createShoppingBasketRequest();
        discountRuleService.applyDiscount(shoppingBasketRequest);
        ArgumentCaptor<Facts> argumentCaptor = ArgumentCaptor.forClass(Facts.class);
        verify(rulesEngine).fire(any(Rules.class), argumentCaptor.capture());
        Facts facts = argumentCaptor.getValue();

        assertEquals(1.5, ((ShoppingBasketRequest) facts.getFact("shoppingBasketRequest").getValue()).getTotalAmount());
    }

    @Test
    void applyDiscount_whenShoppingAddedItems_shouldReturnTotal() {
        ShoppingBasketRequest shoppingBasketRequest = createShoppingBasketRequest();
        BigDecimal actual = discountRuleService.applyDiscount(shoppingBasketRequest);

        assertEquals(1.5, actual.doubleValue());
    }

    private ShoppingBasketRequest createShoppingBasketRequest() {
        List<AddedItem> addedItems = new ArrayList<>();
        addedItems.add(AddedItem.builder().productId("4").numberOfUnits(3).productUnitCost(BigDecimal.valueOf(0.5)).build());
        return ShoppingBasketRequest.builder().addedItems(addedItems).totalAmount(20.0).build();
    }
}