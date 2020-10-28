package co.uk.grocery.henrys.shoppingcart;

import co.uk.grocery.henrys.shoppingcart.service.OnlineShoppingCartService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class OnlineShoppingCartApplicationTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private OnlineShoppingCartApplication onlineShoppingCartApplication;
    private OnlineShoppingCartService onlineShoppingCartService;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
        onlineShoppingCartService = mock(OnlineShoppingCartService.class);
        when(onlineShoppingCartService.calculateShoppingBasket()).thenReturn(false);
        onlineShoppingCartApplication = new OnlineShoppingCartApplication(onlineShoppingCartService);
    }

    @AfterEach
    void setDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void main() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future future = executor.submit(() -> OnlineShoppingCartApplication.main(new String[]{}));
        try {
            future.get(1, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
        }

    }

    @Test
    void run_shouldPrintBanner() {
        onlineShoppingCartApplication.run((""));
        assertTrue(outContent.toString().contains("Welcome to Henry's Grocery Online Shopping Cart"));
    }

    @Test
    void run_shouldInvokeCalculateShoppingBasketOnService() {
        onlineShoppingCartApplication.run((""));

        verify(onlineShoppingCartService, times(1)).calculateShoppingBasket();
    }

}
