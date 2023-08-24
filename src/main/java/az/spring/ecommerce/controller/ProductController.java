package az.spring.ecommerce.controller;

import az.spring.ecommerce.constant.CommerceConstant;
import az.spring.ecommerce.mappers.ProductMapper;
import az.spring.ecommerce.model.Product;
import az.spring.ecommerce.request.ProductRequest;
import az.spring.ecommerce.service.ProductService;
import az.spring.ecommerce.utils.CommerceUtil;
import az.spring.ecommerce.wrapper.ProductWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<String> addProduct(@RequestBody ProductRequest productRequest) {
        try {
            return productService.addProduct(productRequest);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CommerceUtil.getResponseMessage(CommerceConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("get")
    public ResponseEntity<List<ProductWrapper>> getAllProduct() {
        try {
            return productService.getAllProduct();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("update")
    public ResponseEntity<String> updateProduct(@RequestBody ProductRequest productRequest) {
        try {
            return productService.updateProduct(productRequest);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CommerceUtil.getResponseMessage(CommerceConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}