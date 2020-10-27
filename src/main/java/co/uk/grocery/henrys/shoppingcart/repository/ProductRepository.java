package co.uk.grocery.henrys.shoppingcart.repository;

import co.uk.grocery.henrys.shoppingcart.repository.entity.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {
}
