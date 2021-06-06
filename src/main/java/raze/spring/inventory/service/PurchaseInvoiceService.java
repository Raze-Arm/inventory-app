package raze.spring.inventory.service;

import org.springframework.data.domain.Page;
import raze.spring.inventory.domain.dto.PurchaseInvoiceDto;

import java.util.List;
import java.util.UUID;

public interface PurchaseInvoiceService {

    Page<PurchaseInvoiceDto> getInvoicePage(int page, int size, String sort, String search);

    List<PurchaseInvoiceDto> getInvoiceList();

    PurchaseInvoiceDto getInvoice(UUID id);

    UUID saveInvoice(PurchaseInvoiceDto invoiceDto);


    void deleteInvoice(UUID id);

}
