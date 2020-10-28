package co.uk.grocery.henrys.shoppingcart.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

@Entity
@Table(name = "DISCOUNT_TBL")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Discount {

    @Id
    @Column(name = "DISCOUNT_ID", insertable = false, updatable = false)
    private Integer discountId;

    @Column(name = "OFFER")
    private String offer;

    @Column(name = "VALID_FROM")
    private Date validFrom;

    @Column(name = "VALID_TO")
    private Date validTo;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name = "DISCOUNT_ID", referencedColumnName = "DISCOUNT_ID"),
    })
    private Set<DiscountProduct> discountProducts;
}
