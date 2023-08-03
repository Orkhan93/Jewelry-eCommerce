package az.spring.ecommerce.model;

import az.spring.ecommerce.enums.UserRole;
import az.spring.ecommerce.request.UserRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "contactNumber")
    private String contactNumber;

    @Column(name = "password")
    private String password;

    @Column(name = "userRole")
    private String userRole;

    @Column(name = "status")
    private String status;

    @Column(name = "img")
    private byte[] img;

}