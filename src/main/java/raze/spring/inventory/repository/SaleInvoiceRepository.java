package raze.spring.inventory.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import raze.spring.inventory.domain.SaleInvoice;

import java.util.UUID;

@Repository
public interface SaleInvoiceRepository extends JpaRepository<SaleInvoice, UUID> {
    @Query(value = "SELECT S FROM SaleInvoice  S WHERE S.customer.firstName LIKE %:search% OR S.customer.lastName LIKE %:search%")
    Page<SaleInvoice> findAll(Pageable pageable, @Param("search") String search);
}
