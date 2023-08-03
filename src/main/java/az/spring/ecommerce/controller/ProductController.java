package az.spring.ecommerce.controller;

import az.spring.ecommerce.mappers.ProductMapper;
import az.spring.ecommerce.request.ProductRequest;
import az.spring.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/add")
    public String addProduct(@ModelAttribute ProductRequest productRequest){
        productService.addProduct(productRequest);
        return "redirect:/Product"; // bu hisseni duzeltmek lazimdi..
    }

}