package raze.spring.inventory.service;

import org.springframework.data.domain.Page;
import raze.spring.inventory.domain.dto.InvoiceDto;

public interface InvoiceService {
    Page<InvoiceDto> getInvoicePage(int page, int size, String sort, String search);
}
