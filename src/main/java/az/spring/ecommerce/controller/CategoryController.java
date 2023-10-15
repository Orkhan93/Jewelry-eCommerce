package az.spring.ecommerce.controller;

import az.spring.ecommerce.model.Category;
import az.spring.ecommerce.request.CategoryRequest;
import az.spring.ecommerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/add")
    public ResponseEntity<String> addCategory(@RequestBody CategoryRequest categoryRequest) {
            return categoryService.addCategory(categoryRequest);
    }

    @GetMapping("/get")
    public ResponseEntity<List<Category>> getAllCategory(@RequestParam(required = false) String filterValue) {
            return categoryService.getAllCategory(filterValue);
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateCategory(@RequestBody CategoryRequest categoryRequest) {
            return categoryService.updateCategory(categoryRequest);
    }

}