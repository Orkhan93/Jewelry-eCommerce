package az.spring.ecommerce.repository;

import az.spring.ecommerce.model.Product;
import az.spring.ecommerce.wrapper.ProductWrapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<ProductWrapper> getAllProduct();

}