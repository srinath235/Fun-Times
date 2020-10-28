package co.uk.grocery.henrys.shoppingcart.config;

import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertTrue;

class OnlineShoppingCartConfigTest {

    private OnlineShoppingCartConfig onlineShoppingCartConfig;

    @BeforeEach
    void setUp() {
        onlineShoppingCartConfig = new OnlineShoppingCartConfig();
    }

    @Test
    void scanner() {
        assertTrue(onlineShoppingCartConfig.scanner() instanceof Scanner);
    }

    @Test
    void rules() {
        assertTrue(onlineShoppingCartConfig.rules() instanceof Rules);
    }

    @Test
    void rulesEngine() {
        assertTrue(onlineShoppingCartConfig.rulesEngine() instanceof RulesEngine);
    }
}