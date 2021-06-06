package raze.spring.inventory.service;

import org.springframework.data.domain.Page;
import raze.spring.inventory.domain.dto.SaleInvoiceDto;

import java.util.List;
import java.util.UUID;

public interface SaleInvoiceService {
    Page<SaleInvoiceDto> getInvoicePage(int page, int size, String sort, String search);


    List<SaleInvoiceDto> getInvoiceList();

    SaleInvoiceDto getInvoice(UUID id);

    UUID saveInvoice(SaleInvoiceDto saleInvoiceDto);

    void deleteInvoice(UUID id);

}
