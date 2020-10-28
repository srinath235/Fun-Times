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

class OnlineShoppingCartRunnerTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private OnlineShoppingCartService onlineShoppingCartService;
    private OnlineShoppingCartRunner onlineShoppingCartRunner;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
        onlineShoppingCartService = mock(OnlineShoppingCartService.class);
        when(onlineShoppingCartService.calculateShoppingBasket()).thenReturn(false);
        onlineShoppingCartRunner = new OnlineShoppingCartRunner(onlineShoppingCartService);
    }

    @AfterEach
    void setDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }


    @Test
    void run_shouldPrintBanner() {
        onlineShoppingCartRunner.run((""));
        assertTrue(outContent.toString().contains("Welcome to Henry's Grocery Online Shopping Cart"));
    }

    @Test
    void run_shouldInvokeCalculateShoppingBasketOnService() {
        onlineShoppingCartRunner.run((""));

        verify(onlineShoppingCartService, times(1)).calculateShoppingBasket();
    }

    @Test
    void run_shouldInvokeCalculateShoppingBasketOnServiceAndKeepRunning() {
        when(onlineShoppingCartService.calculateShoppingBasket()).thenReturn(false);
        onlineShoppingCartRunner = new OnlineShoppingCartRunner(onlineShoppingCartService);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future future = executor.submit(() -> onlineShoppingCartRunner.run(("")));
        try {
            future.get(1, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
        }

        verify(onlineShoppingCartService).calculateShoppingBasket();
    }

}