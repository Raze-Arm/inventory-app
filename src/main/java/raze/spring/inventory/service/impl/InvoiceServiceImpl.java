package raze.spring.inventory.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import raze.spring.inventory.domain.view.InvoiceView;
import raze.spring.inventory.repository.InvoiceViewRepository;
import raze.spring.inventory.service.InvoiceService;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceViewRepository invoiceViewRepository;

    public InvoiceServiceImpl(InvoiceViewRepository invoiceViewRepository) {
        this.invoiceViewRepository = invoiceViewRepository;
    }

    @Override
    public Page<InvoiceView> getInvoicePage(int page, int size, String sort, String search) {
        final Pageable pageable = PageRequest.of(page, size, Sort.by(sort != null ? sort : "id"));
        if(search == null || search.length() == 0) {
            return  this.invoiceViewRepository.findAll(pageable);
        } else {
            return  this.invoiceViewRepository.findAll(pageable, search);
        }
    }
}
