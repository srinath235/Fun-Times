package co.uk.grocery.henrys.shoppingcart;

import co.uk.grocery.henrys.shoppingcart.service.OnlineShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OnlineShoppingCartRunner implements CommandLineRunner {

    private final OnlineShoppingCartService onlineShoppingCartService;

    @Override
    public void run(String... args) {
        printBanner();
        while (onlineShoppingCartService.calculateShoppingBasket()) ;
    }

    private void printBanner() {
        System.out.println("#########################################################################################");
        System.out.println("Welcome to Henry's Grocery Online Shopping Cart");
        System.out.println("Deals are as below");
        System.out.println("If you are done with the shopping enter 'D' or if you want to quit at any time enter 'Q'");
        System.out.println("#########################################################################################");
    }
}
