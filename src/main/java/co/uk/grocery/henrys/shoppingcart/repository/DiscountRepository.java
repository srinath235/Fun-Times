package co.uk.grocery.henrys.shoppingcart.repository;

import co.uk.grocery.henrys.shoppingcart.repository.entity.Discount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends CrudRepository<Discount, Integer> {
}
