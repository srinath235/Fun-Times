package co.uk.grocery.henrys.shoppingcart.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AddedItem {
    private String productId;
    private int numberOfUnits;
    private BigDecimal productUnitCost;
}
