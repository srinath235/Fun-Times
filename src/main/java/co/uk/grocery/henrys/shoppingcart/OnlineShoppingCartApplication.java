package co.uk.grocery.henrys.shoppingcart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("co.uk.grocery.henrys.shoppingcart.repository")
public class OnlineShoppingCartApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineShoppingCartApplication.class, args);
    }
}
