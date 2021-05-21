package raze.spring.inventory.converter.service;

import raze.spring.inventory.domain.dto.PurchaseInvoiceDto;

import java.util.List;
import java.util.UUID;

public interface PurchaseInvoiceService {
    List<PurchaseInvoiceDto> getInvoiceList();

    PurchaseInvoiceDto getInvoice(UUID id);

    UUID saveInvoice(PurchaseInvoiceDto invoiceDto);


    void deleteInvoice(UUID id);

}
