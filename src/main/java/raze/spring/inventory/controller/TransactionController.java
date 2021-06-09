package raze.spring.inventory.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import raze.spring.inventory.domain.dto.PurchaseTransactionDto;
import raze.spring.inventory.domain.dto.SaleTransactionDto;
import raze.spring.inventory.domain.dto.TransactionDto;
import raze.spring.inventory.service.TransactionService;

@RestController
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }


    @GetMapping(path = {"/transaction", "/transaction/"})
    public ResponseEntity<Page<TransactionDto>> getTrnsactionPage(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size,
            @RequestParam(value = "sort",required = false) String sort,
            @RequestParam(value = "search", required = false) String search) {
        return ResponseEntity.ok(this.transactionService.getTransactionPage(page,size,sort, search));
    }
    @GetMapping(path = {"/transaction/sale", "/transaction/sale/"})
    public ResponseEntity<Page<SaleTransactionDto>> getSaleTransactionPage(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size,
            @RequestParam(value = "sort",required = false) String sort,
            @RequestParam(value = "search", required = false) String search) {
        return ResponseEntity.ok(this.transactionService.getSaleTransactionPage(page,size,sort, search));
    }
    @GetMapping(path = {"/transaction/purchase", "/transaction/purchase/"})
    public ResponseEntity<Page<PurchaseTransactionDto>> getPurchaseTransactionPage(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size,
            @RequestParam(value = "sort",required = false) String sort,
            @RequestParam(value = "search", required = false) String search) {
        return ResponseEntity.ok(this.transactionService.getPurchaseTransaction(page,size,sort, search));
    }

}
