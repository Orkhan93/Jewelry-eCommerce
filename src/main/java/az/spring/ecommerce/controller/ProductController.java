package az.spring.ecommerce.controller;

import az.spring.ecommerce.request.ProductRequest;
import az.spring.ecommerce.service.ProductService;
import az.spring.ecommerce.wrapper.ProductWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<String> addProduct(@RequestBody ProductRequest productRequest) {
            return productService.addProduct(productRequest);
    }

    @GetMapping("get")
    public ResponseEntity<List<ProductWrapper>> getAllProduct() {
            return productService.getAllProduct();
    }

    @PostMapping("update")
    public ResponseEntity<String> updateProduct(@RequestBody ProductRequest productRequest) {
            return productService.updateProduct(productRequest);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
            return productService.deleteProduct(id);
    }

    @PostMapping("updateStatus")
    public ResponseEntity<String> updateStatus(@RequestBody ProductRequest productRequest) {
            return productService.updateStatus(productRequest);
    }

    @GetMapping("/getByCategory/{id}")
    public ResponseEntity<List<ProductWrapper>> getByCategory(@PathVariable Long id) {
            return productService.getByCategory(id);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<ProductWrapper> getProductId(@PathVariable Long id) {
            return productService.getProductId(id);
    }

}