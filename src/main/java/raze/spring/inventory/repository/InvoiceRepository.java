package raze.spring.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raze.spring.inventory.domain.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<String, Invoice> {
}
