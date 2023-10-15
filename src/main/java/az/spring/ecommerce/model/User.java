package az.spring.ecommerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.Data;

@NamedQuery(name = "User.findFirstByEmail" , query = "select  u from User u where u.email=:email")
@NamedQuery(name = "User.getAllUser" , query = "select new az.spring.ecommerce.wrapper.UserWrapper(u.id,u.name,u.email,u.contactNumber,u.status) from User u where u.userRole='user'")
@NamedQuery(name = "User.updateStatus" ,query = "update User u set u.status=:status where u.id=:id")
@NamedQuery(name = "User.getAllAdmin" , query = "select u.email from User u where u.userRole='admin'")

@Entity
@Data
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

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