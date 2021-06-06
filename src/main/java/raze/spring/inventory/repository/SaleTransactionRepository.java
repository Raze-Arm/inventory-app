package raze.spring.inventory.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import raze.spring.inventory.domain.SaleTransaction;

import java.util.UUID;

@Repository
public interface SaleTransactionRepository extends JpaRepository<SaleTransaction, UUID> {


    @Query(value = "SELECT T FROM  SaleTransaction T WHERE T.product.name LIKE %:search%")
    Page<SaleTransaction> findAll(Pageable pageable, @Param("search") String search);
}
