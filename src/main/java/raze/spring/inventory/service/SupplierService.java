package raze.spring.inventory.service;

import org.springframework.data.domain.Page;
import raze.spring.inventory.domain.dto.SupplierDto;

import java.util.List;
import java.util.UUID;

public interface SupplierService {
    Page<SupplierDto> getSupplierPage(int page, int size, String sort, String search);

    List<SupplierDto> getSupplierList();

    SupplierDto getSupplier(UUID id);

    UUID saveSupplier(SupplierDto supplierDto);

    void updateSupplier(SupplierDto supplierDto);

    void deleteSupplier(UUID id);
}
