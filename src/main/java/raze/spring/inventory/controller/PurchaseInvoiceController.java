package raze.spring.inventory.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raze.spring.inventory.service.PurchaseInvoiceService;
import raze.spring.inventory.domain.dto.PurchaseInvoiceDto;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
public class PurchaseInvoiceController {
    private final PurchaseInvoiceService invoiceService;

    public PurchaseInvoiceController(PurchaseInvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping(path = {"/purchase-invoice", "/purchase-invoice/"})
    public ResponseEntity<Page<PurchaseInvoiceDto>> getInvoicePage(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size,
            @RequestParam(value = "sort",required = false) String sort,
            @RequestParam(value = "search", required = false) String search){
        return  ResponseEntity.ok(this.invoiceService.getInvoicePage(page, size, sort, search));
    }

    @GetMapping(path = {"/purchase-invoice", "/purchase-invoice/"}, params = {"search-type=list"})
    public ResponseEntity<List<PurchaseInvoiceDto>> getAllInvoices() {
        return  ResponseEntity.ok(this.invoiceService.getInvoiceList());
    }

    @GetMapping(path = {"/purchase-invoice/{id}", "/purchase-invoice/{id}/"})
    public ResponseEntity<PurchaseInvoiceDto> getInvoice(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(this.invoiceService.getInvoice(id));
    }


    @PostMapping(path = {"/purchase-invoice", "/purchase-invoice/"})
    public ResponseEntity<UUID> saveInvoice(@Valid @RequestBody PurchaseInvoiceDto invoice) {
        return  ResponseEntity.ok(this.invoiceService.saveInvoice(invoice));
    }

    @DeleteMapping(path = {"/purchase-invoice/{id}", "/purchase-invoice/{id}/"})
    public ResponseEntity<Void> deleteInvoice(@PathVariable("id") UUID id) {
        this.invoiceService.deleteInvoice(id);
        return ResponseEntity.ok().build();
    }
}
