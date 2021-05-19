package raze.spring.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raze.spring.inventory.domain.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<String , Transaction> {
}
