package co.uk.grocery.henrys.shoppingcart.service;

import co.uk.grocery.henrys.shoppingcart.model.AddedItem;
import co.uk.grocery.henrys.shoppingcart.model.ShoppingBasketRequest;
import co.uk.grocery.henrys.shoppingcart.repository.DiscountRepository;
import co.uk.grocery.henrys.shoppingcart.repository.ProductRepository;
import co.uk.grocery.henrys.shoppingcart.repository.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class OnlineShoppingCartService {
    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;
    private final Scanner scanner;
    private final DiscountRuleService discountRuleService;

    public boolean calculateShoppingBasket() {
        printDiscountsAvailable();
        Spliterator<Product> productIterator = productRepository.findAll().spliterator();
        if (!isNull(productIterator)) {
            List<Product> products = StreamSupport
                    .stream(productRepository.findAll().spliterator(), false)
                    .collect(Collectors.toList());
            printProductCatalogue(products);
            boolean isDone = false;
            ShoppingBasketRequest shoppingBasketRequest = ShoppingBasketRequest.builder()
                    .addedItems(new ArrayList<>())
                    .totalAmount(0).build();
            while (!isDone) {
                System.out.println("Please select one of the product from above and add it to your basket");
                String input = scanner.next();
                if (validateInputEntered(input, products)) {
                    if (input.equalsIgnoreCase("Q")) {
                        System.out.println("Thank you for shopping with us!");
                        return false;
                    }
                    isDone = input.equalsIgnoreCase("D");
                    if (!isDone) {
                        System.out.println("Please enter the number of units you want to order");
                        try {
                            int numberOfUnits = scanner.nextInt();
                            shoppingBasketRequest.getAddedItems().add(AddedItem.builder()
                                    .numberOfUnits(numberOfUnits)
                                    .productId(input)
                                    .productUnitCost(getCostOfProduct(products, input))
                                    .build());
                        } catch (InputMismatchException ime) {
                            System.err.println("Invalid number of units entered, " + scanner.next());
                        }
                    }
                } else {
                    System.err.println("Invalid product selection, " + input);
                }
            }
            System.out.println("When do you want to buy it from (today (0)/yesterday (-1)/in 5 days time (5)/in 30 days time(+30))");
            try {
                int numberOfDaysFrom = scanner.nextInt();
                shoppingBasketRequest.setBoughtDate(java.sql.Date.valueOf(LocalDate.now().plusDays(numberOfDaysFrom)));
                System.out.println("Total amount payable is £" + discountRuleService.applyDiscount(shoppingBasketRequest));
            } catch (InputMismatchException ime) {
                System.err.println("Invalid days entered, " + scanner.next());
            }
        } else {
            System.err.println("Products are empty");
        }

        return false;
    }

    private void printProductCatalogue(List<Product> products) {
        System.out.println();
        System.out.println("        Product catalogue       ");
        System.out.println("--------------------------------");
        System.out.println();
        products.forEach(product ->
                System.out.println(product.getProductId() + " -> " + product.getProduct() + ", each " + product.getUnit() + " costs £" + product.getCost()));
    }

    private void printDiscountsAvailable() {
        System.out.println();
        System.out.println("        Discounts Available       ");
        System.out.println("----------------------------------");
        System.out.println();
        discountRepository.findAll()
                .forEach(discount ->
                        System.out.println(discount.getOffer() + ", valid from " + discount.getValidFrom() + " until end of " + discount.getValidTo()));
        System.out.println();
    }

    private boolean validateInputEntered(String input, List<Product> products) {
        boolean exists = products.stream().filter(product -> product.getProductId().toString().equals(input)).findFirst().isPresent();
        return exists || Arrays.asList("Q", "q", "D", "d").contains(input);
    }

    private BigDecimal getCostOfProduct(List<Product> products, String productId) {
        return products.stream().filter(product -> product.getProductId().toString().equals(productId)).findFirst().get().getCost();
    }
}
