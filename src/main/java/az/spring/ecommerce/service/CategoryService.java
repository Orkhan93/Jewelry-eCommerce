package az.spring.ecommerce.service;

import az.spring.ecommerce.constant.CommerceConstant;
import az.spring.ecommerce.mappers.CategoryMapper;
import az.spring.ecommerce.model.Category;
import az.spring.ecommerce.repository.CategoryRepository;
import az.spring.ecommerce.request.CategoryRequest;
import az.spring.ecommerce.security.JwtRequestFilter;
import az.spring.ecommerce.utils.CommerceUtil;
import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final JwtRequestFilter jwtRequestFilter;
    private final CategoryMapper categoryMapper;

    public ResponseEntity<String> addCategory(CategoryRequest categoryRequest) {
        try {
            if (jwtRequestFilter.isAdmin()) {
                if (validateCategory(categoryRequest, false)) {
                    categoryRepository.save(getCategoryFromRequest(categoryRequest, false));
                    return CommerceUtil.getResponseMessage("Category Added Successfully.", HttpStatus.OK);
                }
            } else {
                return CommerceUtil.getResponseMessage(CommerceConstant.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CommerceUtil.getResponseMessage(CommerceConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    private boolean validateCategory(CategoryRequest categoryRequest, boolean validateId) {
        if (categoryRequest.getName() != null) {
            if (categoryRequest.getId() != null && validateId) {
                return true;
            } else if (!validateId) {
                return true;
            }
        }
        return false;
    }

    private Category getCategoryFromRequest(CategoryRequest categoryRequest, Boolean isAdd) {
        Category category = new Category();
        if (isAdd) {
            category.setId(categoryRequest.getId());
        }
        category.setName(categoryRequest.getName());
        return category;
    }

    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
        try {
            if (!Strings.isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true")) {
                log.info("Inside if");
                return new ResponseEntity<List<Category>>(categoryRepository.getAllCategory(), HttpStatus.OK);
            }
            return new ResponseEntity<>(categoryRepository.findAll(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<List<Category>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> updateCategory(CategoryRequest categoryRequest) {
        try {
            if (jwtRequestFilter.isAdmin()) {
                if (validateCategory(categoryRequest, true)) {
                    Optional<Category> optionalCategory = categoryRepository.findById(categoryRequest.getId());
                    if (!optionalCategory.isEmpty()) {
                        categoryRepository.save(getCategoryFromRequest(categoryRequest, true));
                        return CommerceUtil.getResponseMessage
                                ("Category Updated Successfully.", HttpStatus.OK);
                    } else {
                        return CommerceUtil.getResponseMessage
                                ("Category id doesn't not exist.", HttpStatus.OK);
                    }
                }
                return CommerceUtil.getResponseMessage(CommerceConstant.INVALID_DATA, HttpStatus.BAD_REQUEST);
            } else
                return CommerceUtil.getResponseMessage(CommerceConstant.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CommerceUtil.getResponseMessage(CommerceConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}