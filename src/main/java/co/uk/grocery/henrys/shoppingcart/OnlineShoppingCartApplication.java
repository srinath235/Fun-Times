package co.uk.grocery.henrys.shoppingcart;

import co.uk.grocery.henrys.shoppingcart.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Arrays;
import java.util.Scanner;

@SpringBootApplication
@EnableJpaRepositories("co.uk.grocery.henrys.shoppingcart.repository")
@Slf4j
public class OnlineShoppingCartApplication implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    public static void main(String[] args) {
        SpringApplication.run(OnlineShoppingCartApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("##################################################");
        System.out.println("Welcome to Henry's Grocery Online Shopping Cart ");
        System.out.println("Deals are as below");
        System.out.println("##################################################");
    }
}
