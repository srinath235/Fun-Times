package co.uk.grocery.henrys.shoppingcart;

import co.uk.grocery.henrys.shoppingcart.repository.entity.Discount;
import co.uk.grocery.henrys.shoppingcart.repository.entity.DiscountProduct;
import co.uk.grocery.henrys.shoppingcart.repository.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestUtils {

    public static List<Discount> createDiscounts() {
        Set<DiscountProduct> discountProducts = new HashSet<>();
        discountProducts.add(DiscountProduct.builder().discountId(1).discountProductId(1).productId(1).build());
        discountProducts.add(DiscountProduct.builder().discountId(1).discountProductId(2).productId(2).build());
        List<Discount> discountList = new ArrayList();
        discountList.add(Discount.builder()
                .offer("Buy 2 tins of soup and get a loaf of bread half price")
                .validTo(getDate(7))
                .validFrom(getDate(-1))
                .discountId(1).discountProducts(discountProducts).build());

        discountProducts = new HashSet<>();
        discountProducts.add(DiscountProduct.builder().discountId(2).discountProductId(3).productId(4).build());

        discountList.add(Discount.builder()
                .offer("Apples have a 10% discount")
                .validTo(getDate((new Long(ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.now().plusMonths(1).atStartOfDay().toLocalDate()))).intValue()
                        + LocalDate.now().plusMonths(1).lengthOfMonth()))
                .validFrom(getDate(3))
                .discountId(2).discountProducts(discountProducts).build());
        return discountList;
    }

    public static List<Product> createProducts() {
        List<Product> products = new ArrayList();
        products.add(Product.builder().productId(1).product("SOUP").unit("TIN").cost(BigDecimal.valueOf(0.65)).build());
        products.add(Product.builder().productId(2).product("BREAD").unit("LOAF").cost(BigDecimal.valueOf(0.80)).build());
        products.add(Product.builder().productId(3).product("MILK").unit("BOTTLE").cost(BigDecimal.valueOf(1.30)).build());
        products.add(Product.builder().productId(4).product("APPLE").unit("SINGLE").cost(BigDecimal.valueOf(0.10)).build());

        return products;
    }


    public static java.sql.Date getDate(int days) {
        return java.sql.Date.valueOf(LocalDate.now().plusDays(days));
    }
}
