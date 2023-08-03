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
    private Integer price;
    private String size;

}