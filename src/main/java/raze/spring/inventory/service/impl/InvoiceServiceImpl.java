package raze.spring.inventory.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import raze.spring.inventory.converter.InvoiceViewToInvoiceDto;
import raze.spring.inventory.domain.dto.InvoiceDto;
import raze.spring.inventory.repository.InvoiceViewRepository;
import raze.spring.inventory.service.InvoiceService;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceViewRepository invoiceViewRepository;
    private final InvoiceViewToInvoiceDto invoiceViewToInvoiceDto;

    public InvoiceServiceImpl(InvoiceViewRepository invoiceViewRepository, InvoiceViewToInvoiceDto invoiceViewToInvoiceDto) {
        this.invoiceViewRepository = invoiceViewRepository;
        this.invoiceViewToInvoiceDto = invoiceViewToInvoiceDto;
    }

    @Override
    public Page<InvoiceDto> getInvoicePage(int page, int size, String sort, String search) {
        final Pageable pageable = PageRequest.of(page, size, Sort.by(sort != null ? sort : "id"));
        if(search == null || search.length() == 0) {
            return  this.invoiceViewRepository.findAll(pageable).map(this.invoiceViewToInvoiceDto::convert);
        } else {
            return  this.invoiceViewRepository.findAll(pageable, search).map(this.invoiceViewToInvoiceDto::convert);
        }
    }
}
