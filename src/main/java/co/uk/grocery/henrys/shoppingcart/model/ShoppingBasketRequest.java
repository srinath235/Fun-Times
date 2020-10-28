package co.uk.grocery.henrys.shoppingcart.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingBasketRequest {

    private List<AddedItem> addedItems;
    private Date boughtDate;
    private double totalAmount;

}
