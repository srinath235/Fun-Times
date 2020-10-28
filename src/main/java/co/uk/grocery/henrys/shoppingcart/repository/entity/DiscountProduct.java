package co.uk.grocery.henrys.shoppingcart.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DISCOUNT_PRODUCT_TBL")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiscountProduct {

    @Id
    @Column(name = "DISC_PROD_ID", insertable = false, updatable = false)
    private Integer discountProductId;

    @Column(name = "DISCOUNT_ID")
    private Integer discountId;

    @Column(name = "PRODUCT_ID")
    private Integer productId;
}
