package raze.spring.inventory.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import raze.spring.inventory.domain.PurchaseInvoice;

import java.util.UUID;

@Repository
public interface PurchaseInvoiceRepository extends JpaRepository<PurchaseInvoice, UUID> {
    @Query(value = "SELECT P FROM PurchaseInvoice P WHERE P.supplier.firstName LIKE %:search% OR P.supplier.lastName LIKE %:search%")
    Page<PurchaseInvoice> findAll(Pageable pageable, @Param("search")String search);

}
