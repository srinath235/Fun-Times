package co.uk.grocery.henrys.shoppingcart.service;

import co.uk.grocery.henrys.shoppingcart.model.ShoppingBasketRequest;
import co.uk.grocery.henrys.shoppingcart.repository.DiscountRepository;
import co.uk.grocery.henrys.shoppingcart.repository.ProductRepository;
import co.uk.grocery.henrys.shoppingcart.repository.entity.Discount;
import co.uk.grocery.henrys.shoppingcart.repository.entity.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OnlineShoppingCartServiceTest {

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
        when(this.productRepository.findAll()).thenReturn(mock(Iterable.class));
        this.discountRepository = mock(DiscountRepository.class);
        when(this.discountRepository.findAll()).thenReturn(mock(Iterable.class));
        this.scanner = mock(Scanner.class);
        this.discountRuleService = mock(DiscountRuleService.class);
        this.onlineShoppingCartService = new OnlineShoppingCartService(
                productRepository, discountRepository,
                scanner, discountRuleService);
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void calculateShoppingBasket_whenCalled_shouldInvokeDiscountRepositoryFindAllMethod() {
        onlineShoppingCartService.calculateShoppingBasket();
        verify(discountRepository, times(1)).findAll();
    }

    @Test
    void calculateShoppingBasket_whenCalled_shouldInvokeProductRepositoryFindAllMethod() {
        onlineShoppingCartService.calculateShoppingBasket();
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void calculateShoppingBasket_whenCalled_shouldPrintAllDiscounts() {
        List discounts = new ArrayList();
        discounts.add(Discount.builder()
                .discountId(1)
                .offer("Everything is free")
                .validFrom(new Date(System.currentTimeMillis()))
                .validTo(new Date(System.currentTimeMillis()))
                .build());
        when(this.discountRepository.findAll()).thenReturn(discounts);
        onlineShoppingCartService.calculateShoppingBasket();
        assertTrue(outContent.toString().contains("Everything is free, valid from "));
    }

    @ParameterizedTest
    @ValueSource(strings = {"q", "Q"})
    void calculateShoppingBasket_whenCalled_shouldAllowQuitUerInput(String input) {

        when(this.scanner.next()).thenReturn(input);
        List products = getProducts();
        when(this.productRepository.findAll()).thenReturn(products);
        onlineShoppingCartService.calculateShoppingBasket();
        assertTrue(outContent.toString().contains("Thank you for shopping with us!"));
    }

    private List getProducts() {
        List products = new ArrayList();
        products.add(Product.builder().productId(1).product("Test").unit("unit").cost(new BigDecimal(1)).build());
        return products;
    }

    @Test
    void calculateShoppingBasket_whenCalled_shouldPrintAllProducts() {
        List products = getProducts();
        when(this.productRepository.findAll()).thenReturn(products);

        when(this.scanner.next()).thenReturn("q");
        onlineShoppingCartService.calculateShoppingBasket();
        assertTrue(outContent.toString().contains("1 -> Test, each unit costs £1"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"d", "D"})
    void calculateShoppingBasket_whenCalled_shouldAllowDoneUerInput(String input) {

        when(this.scanner.next()).thenReturn(input);
        List products = getProducts();
        when(this.productRepository.findAll()).thenReturn(products);
        onlineShoppingCartService.calculateShoppingBasket();
        assertTrue(outContent.toString().contains("Total amount payable is"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"s", "@", "25"})
    void calculateShoppingBasket_whenCalled_shouldNotAllowUerInput(String input) {

        when(this.scanner.next()).thenReturn(input);
        List products = getProducts();
        when(this.productRepository.findAll()).thenReturn(products);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future future = executor.submit(() -> onlineShoppingCartService.calculateShoppingBasket());
        try {
            future.get(1, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
        }
        assertTrue(errContent.toString().contains("Invalid product selection, "));
    }

    @Test
    void calculateShoppingBasket_whenCalled_shouldInvokeDiscountRuleServiceMethod() {
        when(this.scanner.next()).thenReturn("d");
        List products = getProducts();
        when(this.productRepository.findAll()).thenReturn(products);
        onlineShoppingCartService.calculateShoppingBasket();
        verify(this.discountRuleService, times(1)).applyDiscount(any(ShoppingBasketRequest.class));
    }

    @Test
    void calculateShoppingBasket_whenCalled_shouldNotAllowInvalidUnitValue() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("1\nq\nq\n".getBytes());
        this.scanner = new Scanner(byteArrayInputStream);
        this.onlineShoppingCartService = new OnlineShoppingCartService(
                productRepository, discountRepository,
                scanner, discountRuleService);
        List products = getProducts();
        when(this.productRepository.findAll()).thenReturn(products);
        onlineShoppingCartService.calculateShoppingBasket();
        assertTrue(errContent.toString().contains("Invalid number of units entered"));
    }

    @Test
    void calculateShoppingBasket_whenCalled_shouldAllowValidUnitValue() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("1\n2\nq\n".getBytes());
        this.scanner = new Scanner(byteArrayInputStream);
        this.onlineShoppingCartService = new OnlineShoppingCartService(
                productRepository, discountRepository,
                scanner, discountRuleService);
        List products = getProducts();
        when(this.productRepository.findAll()).thenReturn(products);
        onlineShoppingCartService.calculateShoppingBasket();
        assertFalse(errContent.toString().contains("Invalid number of units entered"));
    }

    @Test
    void calculateShoppingBasket_whenCalled_shouldNotAllowInValidDayValue() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("1\n2\nd\ns\n".getBytes());
        this.scanner = new Scanner(byteArrayInputStream);
        this.onlineShoppingCartService = new OnlineShoppingCartService(
                productRepository, discountRepository,
                scanner, discountRuleService);
        List products = getProducts();
        when(this.productRepository.findAll()).thenReturn(products);
        onlineShoppingCartService.calculateShoppingBasket();
        assertTrue(errContent.toString().contains("Invalid days entered, "));
    }

    @Test
    void calculateShoppingBasket_whenCalled_shouldInvokeDiscountServiceMethod() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("1\n2\nd\n0\n".getBytes());
        this.scanner = new Scanner(byteArrayInputStream);
        this.onlineShoppingCartService = new OnlineShoppingCartService(
                productRepository, discountRepository,
                scanner, discountRuleService);
        List products = getProducts();
        when(this.productRepository.findAll()).thenReturn(products);
        onlineShoppingCartService.calculateShoppingBasket();
        ArgumentCaptor<ShoppingBasketRequest> argumentCaptor = ArgumentCaptor.forClass(ShoppingBasketRequest.class);
        verify(discountRuleService).applyDiscount(argumentCaptor.capture());
        ShoppingBasketRequest shoppingBasketRequest = argumentCaptor.getValue();
        assertAll(() -> {
            assertEquals("1", shoppingBasketRequest.getAddedItems().get(0).getProductId());
            assertEquals(2, shoppingBasketRequest.getAddedItems().get(0).getNumberOfUnits());
            assertEquals(java.sql.Date.valueOf(LocalDate.now()), shoppingBasketRequest.getBoughtDate());
        });
    }

    @Test
    void calculateShoppingBasket_whenCalled_shouldPrintTotalAmount() {
        when(this.scanner.next()).thenReturn("d");
        List products = getProducts();
        when(this.productRepository.findAll()).thenReturn(products);
        when(this.discountRuleService.applyDiscount(any(ShoppingBasketRequest.class))).thenReturn(BigDecimal.valueOf(200.08));
        onlineShoppingCartService.calculateShoppingBasket();
        assertTrue(outContent.toString().contains("Total amount payable is £200.08"));
    }

}