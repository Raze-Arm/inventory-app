package raze.spring.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raze.spring.inventory.domain.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<String , Supplier> {
}
