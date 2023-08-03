package az.spring.ecommerce.request;

import az.spring.ecommerce.enums.UserRole;
import lombok.Data;

@Data
public class UserRequest {

    private Long id;
    private String name;
    private String email;
    private String password;
    private UserRole userRole;

}