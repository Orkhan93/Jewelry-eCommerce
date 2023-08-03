package az.spring.ecommerce.request;

import lombok.Data;

@Data
public class AuthenticationRequest {

    private String username;
    private String password;

}