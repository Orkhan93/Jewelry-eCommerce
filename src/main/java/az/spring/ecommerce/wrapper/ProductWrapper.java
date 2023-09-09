package az.spring.ecommerce.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductWrapper {

    private Long id;
    private String name;
    private String description;
    private String size;
    private Integer price;
    private String status;
    private Long categoryId;
    private String categoryName;

    public ProductWrapper(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public ProductWrapper(Long id, String name, String description, Integer price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

}