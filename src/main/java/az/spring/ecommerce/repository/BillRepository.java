package az.spring.ecommerce.repository;

import az.spring.ecommerce.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, Long> {



}