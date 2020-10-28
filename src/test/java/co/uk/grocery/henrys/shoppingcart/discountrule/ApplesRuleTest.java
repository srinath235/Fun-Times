package co.uk.grocery.henrys.shoppingcart.discountrule;

import co.uk.grocery.henrys.shoppingcart.model.AddedItem;
import co.uk.grocery.henrys.shoppingcart.model.ShoppingBasketRequest;
import co.uk.grocery.henrys.shoppingcart.repository.entity.Discount;
import co.uk.grocery.henrys.shoppingcart.repository.entity.DiscountProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ApplesRuleTest {

    private ApplesRule applesRule;
    private Iterable<Discount> discounts;
    private ShoppingBasketRequest shoppingBasketRequest;

    @BeforeEach
    void setUp() {
        applesRule = new ApplesRule();
        discounts = mock(Iterable.class);
        shoppingBasketRequest = mock(ShoppingBasketRequest.class);
    }

    @Test
    void when_shouldInitialiseWithCorrectRuleName() {
        assertEquals("Apples have a 10% discount", applesRule.ruleName);
    }

    @Test
    void when_shouldReturnFalse_whenShoppingRequestIsNull() {
        boolean actual = applesRule.when(null, discounts);
        assertFalse(actual);
    }

    @Test
    void when_shouldReturnFalse_whenDiscountsAreNull() {
        boolean actual = applesRule.when(shoppingBasketRequest, null);
        assertFalse(actual);
    }

    @Test
    void when_shouldReturnFalse_whenDiscountsIteratorIsNull() {
        when(discounts.spliterator()).thenReturn(null);
        boolean actual = applesRule.when(shoppingBasketRequest, discounts);
        assertFalse(actual);
    }

    @Test
    void when_shouldReturnFalse_whenDiscountsDoesntHaveRule() {
        List<Discount> discountList = createDiscount();
        discountList.get(0).setOffer("Test");
        Spliterator<Discount> spliterator = discountList.spliterator();
        when(discounts.spliterator()).thenReturn(spliterator);
        boolean actual = applesRule.when(shoppingBasketRequest, discounts);
        assertFalse(actual);
    }

    @Test
    void when_shouldReturnFalse_whenShoppingDateDoesntFallUnderBrackets() {
        List<Discount> discountList = createDiscount();
        Spliterator<Discount> spliterator = discountList.spliterator();
        when(discounts.spliterator()).thenReturn(spliterator);
        when(shoppingBasketRequest.getBoughtDate()).thenReturn(getDate(-2));
        boolean actual = applesRule.when(shoppingBasketRequest, discounts);
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
        boolean actual = applesRule.when(shoppingBasketRequest, discounts);
        assertTrue(actual);
    }

    @Test
    void then_shouldApplyDiscount_whenProductIdMatches() {
        List<AddedItem> addedItems = createAddedItems();
        ShoppingBasketRequest shoppingBasketRequest = ShoppingBasketRequest.builder().addedItems(addedItems).totalAmount(20.0).build();

        applesRule.then(shoppingBasketRequest);

        assertEquals(19.85, shoppingBasketRequest.getTotalAmount());
    }

    @Test
    void then_shouldApplyDiscount_whenProductIdMatchesMultipleTimes() {
        List<AddedItem> addedItems = createAddedItems();
        addedItems.add(AddedItem.builder().productId("4").numberOfUnits(10).productUnitCost(BigDecimal.valueOf(0.5)).build());
        ShoppingBasketRequest shoppingBasketRequest = ShoppingBasketRequest.builder().addedItems(addedItems).totalAmount(20.0).build();

        applesRule.then(shoppingBasketRequest);

        assertEquals(19.35, shoppingBasketRequest.getTotalAmount());
    }

    @Test
    void then_shouldApplyDiscount_whenProductIdDoesntMatches() {
        List<AddedItem> addedItems = createAddedItems();
        addedItems.get(0).setProductId("5");
        ShoppingBasketRequest shoppingBasketRequest = ShoppingBasketRequest.builder().addedItems(addedItems).totalAmount(20.0).build();

        applesRule.then(shoppingBasketRequest);

        assertEquals(20, shoppingBasketRequest.getTotalAmount());
    }

    private java.sql.Date getDate(int days) {
        return java.sql.Date.valueOf(LocalDate.now().plusDays(days));
    }

    private List<Discount> createDiscount() {
        Set<DiscountProduct> discountProducts = new HashSet<>();
        discountProducts.add(DiscountProduct.builder().discountId(1).discountProductId(1).productId(4).build());
        List<Discount> discountList = new ArrayList();
        discountList.add(Discount.builder()
                .offer("Apples have a 10% discount")
                .validTo(getDate(3))
                .validFrom(getDate(-1))
                .discountId(1).discountProducts(discountProducts).build());
        return discountList;
    }

    private List<AddedItem> createAddedItems() {
        List<AddedItem> addedItems = new ArrayList<>();
        addedItems.add(AddedItem.builder().productId("4").numberOfUnits(3).productUnitCost(BigDecimal.valueOf(0.5)).build());
        return addedItems;
    }
}