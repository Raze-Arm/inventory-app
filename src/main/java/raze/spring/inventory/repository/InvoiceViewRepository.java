package raze.spring.inventory.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import raze.spring.inventory.domain.view.InvoiceView;

import java.util.UUID;

@Repository
public interface InvoiceViewRepository extends JpaRepository<InvoiceView, UUID> {
    @Query(value = "SELECT I FROM InvoiceView I WHERE I.name LIKE %:search% OR I.type LIKE %:search% ")
    Page<InvoiceView> findAll(Pageable pageable , @Param("search") String search);
}
