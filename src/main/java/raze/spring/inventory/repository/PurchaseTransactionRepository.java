package raze.spring.inventory.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import raze.spring.inventory.domain.PurchaseTransaction;

import java.util.UUID;

@Repository
public interface PurchaseTransactionRepository extends JpaRepository<PurchaseTransaction , UUID> {
    @Query(value = "SELECT T FROM PurchaseTransaction  T WHERE T.product.name LIKE  %:search ")
    Page<PurchaseTransaction> findAll(Pageable pageable, @Param("search")String search);
}
