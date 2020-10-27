package co.uk.grocery.henrys.shoppingcart.repository.entity;

import javax.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name="PRODUCT_TBL")
@Data
public class Product {

    @Id
    @Column(name="PRODUCT_ID", insertable = false, updatable = false)
    private Integer productId;

    @Column(name="PRODUCT")
    private String product;

    @Column(name="UNIT")
    private String unit;

    @Column(name="COST")
    private BigDecimal cost;

}
