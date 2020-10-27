package co.uk.grocery.henrys.shoppingcart.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ShoppingBasketRequest {

    private List<AddedItem> addedItems;
    private Date boughtDate;
}
