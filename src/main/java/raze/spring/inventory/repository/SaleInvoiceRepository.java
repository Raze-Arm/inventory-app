package raze.spring.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raze.spring.inventory.domain.SaleInvoice;

import java.util.UUID;

@Repository
public interface SaleInvoiceRepository extends JpaRepository<SaleInvoice, UUID> {
}
