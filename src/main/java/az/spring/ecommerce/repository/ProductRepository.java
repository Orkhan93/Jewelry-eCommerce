package az.spring.ecommerce.repository;

import az.spring.ecommerce.model.Product;
import az.spring.ecommerce.wrapper.ProductWrapper;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<ProductWrapper> getAllProduct();

    @Modifying
    @Transactional
    Integer updateProductStatus(@Param("status") String status, @Param("id") Long id);

    List<ProductWrapper> getProductByCategory(@Param("id") Long id);

    ProductWrapper getProductId(@Param("id") Long id);

}