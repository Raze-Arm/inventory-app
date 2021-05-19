package raze.spring.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raze.spring.inventory.domain.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<String , Customer> {
}
