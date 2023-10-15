package az.spring.ecommerce.request;

import lombok.Data;

@Data
public class UserSignUpRequest {

    private Integer id;
    private String name;
    private String email;
    private String contactNumber;
    private String password;
    private final String status = "false";
    private final String userRole = "user";
    private byte[] img;

}