package co.uk.grocery.henrys.shoppingcart.config;

import co.uk.grocery.henrys.shoppingcart.discountrule.ApplesRule;
import co.uk.grocery.henrys.shoppingcart.discountrule.TwoTinsOfSoupRule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;

@Configuration
public class OnlineShoppingCartConfig {

    @Bean
    public Scanner scanner() {
        return new Scanner(System.in);
    }

    @Bean
    public Rules rules() {
        Rules rules = new Rules();
        rules.register(new TwoTinsOfSoupRule());
        rules.register(new ApplesRule());
        return rules;
    }

    @Bean
    public RulesEngine rulesEngine() {
        return new DefaultRulesEngine();
    }


}
