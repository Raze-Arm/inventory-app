package raze.spring.inventory.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raze.spring.inventory.service.SaleInvoiceService;
import raze.spring.inventory.domain.dto.SaleInvoiceDto;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping("/v1")
public class SaleInvoiceController {
    private final SaleInvoiceService invoiceService;

    public SaleInvoiceController(SaleInvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping(path = {"/sale-invoice", "/sale-invoice/"})
    public ResponseEntity<Page<SaleInvoiceDto>> getInvoicePage(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size,
            @RequestParam(value = "sort",required = false) String sort,
            @RequestParam(value = "search", required = false) String search) {
        return ResponseEntity.ok(this.invoiceService.getInvoicePage(page,size,sort, search));
    }
    @GetMapping(path = {"/sale-invoice", "/sale-invoice/"}, params = {"search-type=list"})
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
