package raze.spring.inventory.service;

import raze.spring.inventory.domain.dto.SupplierDto;

import java.util.List;
import java.util.UUID;

public interface SupplierService {
    List<SupplierDto> getSupplierList();

    SupplierDto getSupplier(UUID id);

    UUID saveSupplier(SupplierDto supplierDto);

    void updateSupplier(SupplierDto supplierDto);

    void deleteSupplier(UUID id);
}
