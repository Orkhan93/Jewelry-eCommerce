package az.spring.ecommerce.mappers;

import az.spring.ecommerce.model.Product;
import az.spring.ecommerce.request.ProductRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    Product fromProductRequestToProduct(ProductRequest productRequest);
    ProductRequest fromProductToProductRequest(Product product);

}