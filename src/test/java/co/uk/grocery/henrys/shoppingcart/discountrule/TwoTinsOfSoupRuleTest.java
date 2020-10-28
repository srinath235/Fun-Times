package co.uk.grocery.henrys.shoppingcart.discountrule;

import co.uk.grocery.henrys.shoppingcart.model.AddedItem;
import co.uk.grocery.henrys.shoppingcart.model.ShoppingBasketRequest;
import co.uk.grocery.henrys.shoppingcart.repository.entity.Discount;
import co.uk.grocery.henrys.shoppingcart.repository.entity.DiscountProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static co.uk.grocery.henrys.shoppingcart.TestUtils.getDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TwoTinsOfSoupRuleTest {

    private TwoTinsOfSoupRule twoTinsOfSoupRule;
    private Iterable<Discount> discounts;
    private ShoppingBasketRequest shoppingBasketRequest;

    @BeforeEach
    void setUp() {
        twoTinsOfSoupRule = new TwoTinsOfSoupRule();
        discounts = mock(Iterable.class);
        shoppingBasketRequest = mock(ShoppingBasketRequest.class);
    }

    @Test
    void when_shouldInitialiseWithCorrectRuleName() {
        assertEquals("Buy 2 tins of soup and get a loaf of bread half price", twoTinsOfSoupRule.ruleName);
    }

    @Test
    void when_shouldReturnFalse_whenShoppingRequestIsNull() {
        boolean actual = twoTinsOfSoupRule.when(null, discounts);
        assertFalse(actual);
    }

    @Test
    void when_shouldReturnFalse_whenDiscountsAreNull() {
        boolean actual = twoTinsOfSoupRule.when(shoppingBasketRequest, null);
        assertFalse(actual);
    }

    @Test
    void when_shouldReturnFalse_whenDiscountsIteratorIsNull() {
        when(discounts.spliterator()).thenReturn(null);
        boolean actual = twoTinsOfSoupRule.when(shoppingBasketRequest, discounts);
        assertFalse(actual);
    }

    @Test
    void when_shouldReturnFalse_whenDiscountsDoesntHaveRule() {
        List<Discount> discountList = createDiscount();
        discountList.get(0).setOffer("Test");
        Spliterator<Discount> spliterator = discountList.spliterator();
        when(discounts.spliterator()).thenReturn(spliterator);
        boolean actual = twoTinsOfSoupRule.when(shoppingBasketRequest, discounts);
        assertFalse(actual);
    }

    @Test
    void when_shouldReturnFalse_whenShoppingDateDoesntFallUnderBrackets() {
        List<Discount> discountList = createDiscount();
        Spliterator<Discount> spliterator = discountList.spliterator();
        when(discounts.spliterator()).thenReturn(spliterator);
        when(shoppingBasketRequest.getBoughtDate()).thenReturn(getDate(-2));
        boolean actual = twoTinsOfSoupRule.when(shoppingBasketRequest, discounts);
        assertFalse(actual);
    }

    @Test
    void when_shouldReturnTrue_whenShoppingProductIdMatchesInDiscounts() {
        List<Discount> discountList = createDiscount();
        Spliterator<Discount> spliterator = discountList.spliterator();
        when(discounts.spliterator()).thenReturn(spliterator);
        when(shoppingBasketRequest.getBoughtDate()).thenReturn(getDate(0));
        List<AddedItem> addedItems = createAddedItems();
        when(shoppingBasketRequest.getAddedItems()).thenReturn(addedItems);
        boolean actual = twoTinsOfSoupRule.when(shoppingBasketRequest, discounts);
        assertTrue(actual);
    }

    @Test
    void then_shouldApplyDiscount_whenProductIdMatches() {
        List<AddedItem> addedItems = createAddedItems();
        ShoppingBasketRequest shoppingBasketRequest = ShoppingBasketRequest.builder().addedItems(addedItems).totalAmount(20.0).build();

        twoTinsOfSoupRule.then(shoppingBasketRequest);

        assertEquals(19.95, shoppingBasketRequest.getTotalAmount());
    }

    @Test
    void then_shouldApplyDiscount_whenProductIdMatchesMultipleTimes() {
        List<AddedItem> addedItems = createAddedItems();
        addedItems.add(AddedItem.builder().productId("1").numberOfUnits(10).productUnitCost(BigDecimal.valueOf(0.5)).build());
        addedItems.add(AddedItem.builder().productId("2").numberOfUnits(6).productUnitCost(BigDecimal.valueOf(0.1)).build());
        ShoppingBasketRequest shoppingBasketRequest = ShoppingBasketRequest.builder().addedItems(addedItems).totalAmount(20.0).build();

        twoTinsOfSoupRule.then(shoppingBasketRequest);

        assertEquals(19.7, shoppingBasketRequest.getTotalAmount());
    }

    @Test
    void then_shouldApplyDiscount_whenProductIdDoesntMatches() {
        List<AddedItem> addedItems = createAddedItems();
        addedItems.get(0).setProductId("5");
        ShoppingBasketRequest shoppingBasketRequest = ShoppingBasketRequest.builder().addedItems(addedItems).totalAmount(20.0).build();

        twoTinsOfSoupRule.then(shoppingBasketRequest);

        assertEquals(20, shoppingBasketRequest.getTotalAmount());
    }

    @Test
    void then_shouldApplyDiscount_whenLoavesAreLess() {
        List<AddedItem> addedItems = createAddedItems();
        addedItems.get(0).setNumberOfUnits(20);
        addedItems.get(1).setNumberOfUnits(2);
        ShoppingBasketRequest shoppingBasketRequest = ShoppingBasketRequest.builder().addedItems(addedItems).totalAmount(20.0).build();

        twoTinsOfSoupRule.then(shoppingBasketRequest);

        assertEquals(19.8, shoppingBasketRequest.getTotalAmount());
    }

    private List<Discount> createDiscount() {
        Set<DiscountProduct> discountProducts = new HashSet<>();
        discountProducts.add(DiscountProduct.builder().discountId(1).discountProductId(1).productId(1).build());
        discountProducts.add(DiscountProduct.builder().discountId(1).discountProductId(1).productId(2).build());
        List<Discount> discountList = new ArrayList();
        discountList.add(Discount.builder()
                .offer("Buy 2 tins of soup and get a loaf of bread half price")
                .validTo(getDate(3))
                .validFrom(getDate(-1))
                .discountId(1).discountProducts(discountProducts).build());
        return discountList;
    }

    private List<AddedItem> createAddedItems() {
        List<AddedItem> addedItems = new ArrayList<>();
        addedItems.add(AddedItem.builder().productId("1").numberOfUnits(3).productUnitCost(BigDecimal.valueOf(0.5)).build());
        addedItems.add(AddedItem.builder().productId("2").numberOfUnits(2).productUnitCost(BigDecimal.valueOf(0.1)).build());
        return addedItems;
    }

}