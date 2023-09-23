package az.spring.ecommerce.service;

import az.spring.ecommerce.constant.CommerceConstant;
import az.spring.ecommerce.model.Category;
import az.spring.ecommerce.model.Product;
import az.spring.ecommerce.repository.ProductRepository;
import az.spring.ecommerce.request.ProductRequest;
import az.spring.ecommerce.security.JwtRequestFilter;
import az.spring.ecommerce.utils.CommerceUtil;
import az.spring.ecommerce.wrapper.ProductWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productsRepository;
    private final JwtRequestFilter jwtRequestFilter;

    public ResponseEntity<String> addProduct(ProductRequest productRequest) {
            if (jwtRequestFilter.isAdmin()) {
                if (validateProductRequest(productRequest, false)) {
                    productsRepository.save(getProductFromRequest(productRequest, false));
                    return CommerceUtil.getResponseMessage("Product Added Successfully.", HttpStatus.OK);
                }
                return CommerceUtil.getResponseMessage(CommerceConstant.INVALID_DATA, HttpStatus.BAD_REQUEST);
            } else
                return CommerceUtil.getResponseMessage(CommerceConstant.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<List<ProductWrapper>> getAllProduct() {
            return new ResponseEntity<>(productsRepository.getAllProduct(), HttpStatus.OK);
    }

    public ResponseEntity<String> updateProduct(ProductRequest productRequest) {
            if (jwtRequestFilter.isAdmin()) {
                if (validateProductRequest(productRequest, true)) {
                    Optional<Product> optionalProduct = productsRepository.findById(productRequest.getId());
                    if (!optionalProduct.isEmpty()) {
                        Product product = getProductFromRequest(productRequest, true);
                        product.setStatus(optionalProduct.get().getStatus());
                        productsRepository.save(product);
                        return CommerceUtil.getResponseMessage("Product Updated Successfully.", HttpStatus.OK);
                    } else
                        return CommerceUtil.getResponseMessage(CommerceConstant.PRODUCT_ID_DOES_NOT_EXIST, HttpStatus.OK);
                } else
                    return CommerceUtil.getResponseMessage(CommerceConstant.INVALID_DATA, HttpStatus.BAD_REQUEST);
            } else
                return CommerceUtil.getResponseMessage(CommerceConstant.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<String> deleteProduct(Long id) {
            if (jwtRequestFilter.isAdmin()) {
                Optional<Product> optionalProduct = productsRepository.findById(id);
                if (!optionalProduct.isEmpty()) {
                    productsRepository.deleteById(id);
                    return CommerceUtil.getResponseMessage("Product Deleted Successfully.", HttpStatus.OK);
                } else
                    return CommerceUtil.getResponseMessage(CommerceConstant.PRODUCT_ID_DOES_NOT_EXIST, HttpStatus.OK);
            } else
                return CommerceUtil.getResponseMessage(CommerceConstant.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<String> updateStatus(ProductRequest productRequest) {
            if (jwtRequestFilter.isAdmin()) {
                Optional<Product> optionalProduct = productsRepository.findById(productRequest.getId());
                if (!optionalProduct.isEmpty()) {
                    productsRepository.updateProductStatus(productRequest.getStatus(), productRequest.getId());
                    return CommerceUtil.getResponseMessage("Product Status Updated Successfully.", HttpStatus.OK);
                } else
                    return CommerceUtil.getResponseMessage(CommerceConstant.PRODUCT_ID_DOES_NOT_EXIST, HttpStatus.OK);
            } else
                return CommerceUtil.getResponseMessage(CommerceConstant.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<List<ProductWrapper>> getByCategory(Long id) {
            return new ResponseEntity<>(productsRepository.getProductByCategory(id), HttpStatus.OK);
    }

    public ResponseEntity<ProductWrapper> getProductId(Long id) {
            return new ResponseEntity<>(productsRepository.getProductId(id), HttpStatus.OK);
    }

    private boolean validateProductRequest(ProductRequest productRequest, boolean validateId) {
        if (productRequest.getName() != null) {
            if (productRequest.getId() != null && validateId) {
                return true;
            } else if (!validateId) {
                return true;
            }
        }
        return false;
    }

    private Product getProductFromRequest(ProductRequest productRequest, boolean isAdd) {
        Category category = new Category();
        category.setId(productRequest.getCategoryId());

        Product product = new Product();
        if (isAdd) {
            product.setId(productRequest.getId());
        } else {
            product.setStatus("true");
        }
        product.setCategory(category);
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setSize(productRequest.getSize());
        return product;
    }

}