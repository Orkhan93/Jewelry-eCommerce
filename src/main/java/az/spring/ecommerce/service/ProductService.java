package az.spring.ecommerce.service;

import az.spring.ecommerce.mappers.ProductMapper;
import az.spring.ecommerce.repository.ProductRepository;
import az.spring.ecommerce.request.ProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productsRepository;
    private final ProductMapper productMapper;

    public void addProduct(@RequestBody ProductRequest productRequest) {
        productsRepository.save(productMapper.fromProductRequestToProduct(productRequest));
    }

}