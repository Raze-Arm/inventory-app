package raze.spring.inventory.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import raze.spring.inventory.domain.view.TransactionView;

import java.util.UUID;

@Repository
public interface TransactionViewRepository extends JpaRepository<TransactionView, UUID> {
    @Query(value = "SELECT T FROM TransactionView T WHERE T.productName LIKE %:search% OR T.type LIKE %:search% ")
    Page<TransactionView> findAll(Pageable pageable , @Param("search") String search);
}
