package co.uk.grocery.henrys.shoppingcart.repository.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name="DISCOUNT_TBL")
@Data
public class Discount {

    @Id
    @Column(name="DISCOUNT_ID", insertable = false, updatable = false)
    private Integer discountId;

    @Column(name="OFFER")
    private String offer;

    @Column(name="VALID_FROM")
    private Date validFrom;

    @Column(name="VALID_TO")
    private Date validTo;
}
