package raze.spring.inventory.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import raze.spring.inventory.domain.dto.InvoiceDto;

import raze.spring.inventory.service.InvoiceService;

@RestController
public class InvoiceController {
    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping(path = {"/invoice", "/invoice/"})
    public ResponseEntity<Page<InvoiceDto>> getCustomerPage(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size,
            @RequestParam(value = "sort",required = false) String sort,
            @RequestParam(value = "search", required = false) String search) {
        return ResponseEntity.ok(this.invoiceService.getInvoicePage(page,size,sort, search));
    }

}
