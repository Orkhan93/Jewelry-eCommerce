package az.spring.ecommerce.mappers;

import az.spring.ecommerce.model.Category;
import az.spring.ecommerce.request.CategoryRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    Category fromCategoryRequestToModel(CategoryRequest categoryRequest);

    CategoryRequest fromModelToCategoryRequest(Category category);

}