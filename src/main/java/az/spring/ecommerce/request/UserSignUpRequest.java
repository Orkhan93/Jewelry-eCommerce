package az.spring.ecommerce.request;

import lombok.Data;

@Data
public class UserSignUpRequest {

    private String name;
    private String email;
    private String contactNumber;
    private String password;
    private final String status = "false";
    private String userRole;
    private byte[] img;

}