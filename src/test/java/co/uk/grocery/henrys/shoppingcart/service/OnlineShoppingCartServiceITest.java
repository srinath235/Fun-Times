package co.uk.grocery.henrys.shoppingcart.service;

import co.uk.grocery.henrys.shoppingcart.config.OnlineShoppingCartConfig;
import co.uk.grocery.henrys.shoppingcart.repository.DiscountRepository;
import co.uk.grocery.henrys.shoppingcart.repository.ProductRepository;
import co.uk.grocery.henrys.shoppingcart.repository.entity.Discount;
import co.uk.grocery.henrys.shoppingcart.repository.entity.DiscountProduct;
import co.uk.grocery.henrys.shoppingcart.repository.entity.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static co.uk.grocery.henrys.shoppingcart.TestUtils.createDiscounts;
import static co.uk.grocery.henrys.shoppingcart.TestUtils.createProducts;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OnlineShoppingCartServiceITest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private ProductRepository productRepository;
    private DiscountRepository discountRepository;
    private Scanner scanner;
    private DiscountRuleService discountRuleService;
    private OnlineShoppingCartService onlineShoppingCartService;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
        this.productRepository = mock(ProductRepository.class);
        when(this.productRepository.findAll()).thenReturn(createProducts());
        this.discountRepository = mock(DiscountRepository.class);
        when(this.discountRepository.findAll()).thenReturn(createDiscounts());
        this.scanner = mock(Scanner.class);
        OnlineShoppingCartConfig onlineShoppingCartConfig = new OnlineShoppingCartConfig();
        this.discountRuleService = new DiscountRuleService(onlineShoppingCartConfig.rules(),
                onlineShoppingCartConfig.rulesEngine(), discountRepository);
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void three_tins_of_soup_and_two_loaves_of_bread_bought_today() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(("1\n3\n2\n2\nd\n0\n").getBytes());
        this.scanner = new Scanner(byteArrayInputStream);
        this.onlineShoppingCartService = new OnlineShoppingCartService(
                productRepository, discountRepository,
                scanner, discountRuleService);
        boolean actual = onlineShoppingCartService.calculateShoppingBasket();
        assertAll(() -> {
            assertFalse(actual);
            assertTrue(outContent.toString().contains("Total amount payable is £3.15"));
        });
    }

    @Test
    void six_apples_and_bottle_of_milk_bought_today() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(("3\n1\n4\n6\nd\n0\n").getBytes());
        this.scanner = new Scanner(byteArrayInputStream);
        this.onlineShoppingCartService = new OnlineShoppingCartService(
                productRepository, discountRepository,
                scanner, discountRuleService);
        boolean actual = onlineShoppingCartService.calculateShoppingBasket();
        assertAll(() -> {
            assertFalse(actual);
            assertTrue(outContent.toString().contains("Total amount payable is £1.90"));
        });
    }

    @Test
    void six_apples_and_bottle_of_milk_bought_in_five_days_time() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(("3\n1\n4\n6\nd\n5\n").getBytes());
        this.scanner = new Scanner(byteArrayInputStream);
        this.onlineShoppingCartService = new OnlineShoppingCartService(
                productRepository, discountRepository,
                scanner, discountRuleService);
        boolean actual = onlineShoppingCartService.calculateShoppingBasket();
        assertAll(() -> {
            assertFalse(actual);
            assertTrue(outContent.toString().contains("Total amount payable is £1.84"));
        });
    }

    @Test
    void three_apples_two_tins_of_soup_and_a_loaf_of_bread_bought_in_five_days_time() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(("1\n2\n2\n1\n4\n3\nd\n5\n").getBytes());
        this.scanner = new Scanner(byteArrayInputStream);
        this.onlineShoppingCartService = new OnlineShoppingCartService(
                productRepository, discountRepository,
                scanner, discountRuleService);
        boolean actual = onlineShoppingCartService.calculateShoppingBasket();
        assertAll(() -> {
            assertFalse(actual);
            assertTrue(outContent.toString().contains("Total amount payable is £1.97"));
        });
    }

}