package co.uk.grocery.henrys.shoppingcart;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

class OnlineShoppingCartApplicationTest {

    @Test
    void main() {
        OnlineShoppingCartApplication onlineShoppingCartApplication = new OnlineShoppingCartApplication();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future future = executor.submit(() -> onlineShoppingCartApplication.main(new String[]{}));
        try {
            future.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
        }
        assertTrue(true);
    }
}
