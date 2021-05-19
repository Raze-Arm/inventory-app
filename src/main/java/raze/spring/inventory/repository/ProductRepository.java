package raze.spring.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raze.spring.inventory.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<String , Product> {
}
