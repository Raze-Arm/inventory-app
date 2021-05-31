package raze.spring.inventory.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raze.spring.inventory.service.SaleInvoiceService;
import raze.spring.inventory.domain.dto.SaleInvoiceDto;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
public class SaleInvoiceController {
    private final SaleInvoiceService invoiceService;

    public SaleInvoiceController(SaleInvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }


    @GetMapping(path = {"/sale-invoice", "/sale-invoice/"})
    public ResponseEntity<List<SaleInvoiceDto>> getAllInvoices() {
        return ResponseEntity.ok(this.invoiceService.getInvoiceList());
    }

    @GetMapping(path = {"/sale-invoice/{id}", "/sale-invoice/{id}/"})
    public ResponseEntity<SaleInvoiceDto> getInvoice(@PathVariable UUID id) {
        return ResponseEntity.ok(this.invoiceService.getInvoice(id));
    }


    @PostMapping(path = {"/sale-invoice", "/sale-invoice/"})
    public ResponseEntity<UUID> saveInvoice(@Valid @RequestBody SaleInvoiceDto invoice) {
        return ResponseEntity.ok(this.invoiceService.saveInvoice(invoice));
    }


    @DeleteMapping(path = {"/sale-invoice/{id}", "/sale-invoice/{id}/"})
    public ResponseEntity<Void> deleteInvoice(@PathVariable("id") UUID id)  {
        this.invoiceService.deleteInvoice(id);
        return ResponseEntity.ok().build();
    }
}
