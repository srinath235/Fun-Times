package co.uk.grocery.henrys.shoppingcart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

@Configuration
@Profile("test")
public class TestOnlineShoppingCartConfig {

    @Bean
    public Scanner scanner() {
        ByteArrayInputStream in = new ByteArrayInputStream("1\n3\n2\n2\nd\n0\n".getBytes());
        System.setIn(in);
        return new Scanner(System.in);
    }

}
