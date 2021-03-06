package co.uk.grocery.henrys.shoppingcart.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "PRODUCT_TBL")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @Column(name = "PRODUCT_ID", insertable = false, updatable = false)
    private Integer productId;

    @Column(name = "PRODUCT")
    private String product;

    @Column(name = "UNIT")
    private String unit;

    @Column(name = "COST")
    private BigDecimal cost;

}
