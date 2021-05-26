package raze.spring.inventory.service;

import org.springframework.data.domain.Page;
import raze.spring.inventory.domain.view.InvoiceView;

public interface InvoiceService {
    Page<InvoiceView> getInvoicePage(int page, int size, String sort, String search);
}
