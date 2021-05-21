package raze.spring.inventory.converter.service;

import raze.spring.inventory.domain.dto.SaleInvoiceDto;

import java.util.List;
import java.util.UUID;

public interface SaleInvoiceService {

    List<SaleInvoiceDto> getInvoiceList();

    SaleInvoiceDto getInvoice(UUID id);

    UUID saveInvoice(SaleInvoiceDto saleInvoiceDto);

    void deleteInvoice(UUID id);

}
