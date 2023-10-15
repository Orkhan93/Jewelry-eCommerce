package az.spring.ecommerce.repository;

import az.spring.ecommerce.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Long> {

    List<Bill> getAllBills();

    List<Bill> getBillByUserName(@Param("username") String username);

}