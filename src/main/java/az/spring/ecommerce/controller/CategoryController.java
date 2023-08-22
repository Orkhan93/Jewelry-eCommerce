package az.spring.ecommerce.controller;

import az.spring.ecommerce.constant.CommerceConstant;
import az.spring.ecommerce.model.Category;
import az.spring.ecommerce.request.CategoryRequest;
import az.spring.ecommerce.service.CategoryService;
import az.spring.ecommerce.utils.CommerceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/add")
    public ResponseEntity<String> addCategory(@RequestBody CategoryRequest categoryRequest) {
        try {
            return categoryService.addCategory(categoryRequest);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CommerceUtil.getResponseMessage(CommerceConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/get")
    public ResponseEntity<List<Category>> getAllCategory(@RequestParam(required = false) String filterValue) {
        try {
            return categoryService.getAllCategory(filterValue);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateCategory(@RequestBody CategoryRequest categoryRequest) {
        try {
            return categoryService.updateCategory(categoryRequest);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CommerceUtil.getResponseMessage(CommerceConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}