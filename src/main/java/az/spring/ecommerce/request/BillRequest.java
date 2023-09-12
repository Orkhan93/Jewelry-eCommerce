package az.spring.ecommerce.request;

import lombok.Data;

@Data
public class BillRequest {

    private Long id;
    private String uuid;
    private String name;
    private String email;
    private String contactNumber;
    private String paymentMethod;
    private Integer total;
    private String productDetail;
    private String createdBy;

}