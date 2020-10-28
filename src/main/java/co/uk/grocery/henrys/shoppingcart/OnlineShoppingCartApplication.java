package co.uk.grocery.henrys.shoppingcart;

import co.uk.grocery.henrys.shoppingcart.service.OnlineShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("co.uk.grocery.henrys.shoppingcart.repository")
public class OnlineShoppingCartApplication implements CommandLineRunner {
    @Autowired
    private OnlineShoppingCartService onlineShoppingCartService;

    public OnlineShoppingCartApplication() {
    }

    public OnlineShoppingCartApplication(OnlineShoppingCartService onlineShoppingCartService) {
        this.onlineShoppingCartService = onlineShoppingCartService;
    }

    public static void main(String[] args) {
        SpringApplication.run(OnlineShoppingCartApplication.class, args);
    }

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
