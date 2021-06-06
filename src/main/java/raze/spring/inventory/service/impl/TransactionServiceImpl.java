package raze.spring.inventory.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import raze.spring.inventory.converter.PurchaseTransactionToPurchaseTransactionDto;
import raze.spring.inventory.converter.SaleTransactionToSaleTransactionDto;
import raze.spring.inventory.domain.dto.PurchaseTransactionDto;
import raze.spring.inventory.domain.dto.SaleTransactionDto;
import raze.spring.inventory.domain.view.TransactionView;
import raze.spring.inventory.repository.PurchaseTransactionRepository;
import raze.spring.inventory.repository.SaleTransactionRepository;
import raze.spring.inventory.repository.TransactionViewRepository;
import raze.spring.inventory.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionViewRepository transactionViewRepository;
    private final PurchaseTransactionRepository purchaseTrRepository;
    private final SaleTransactionRepository saleTrRepository;
    private final PurchaseTransactionToPurchaseTransactionDto purchaseToPurchaseDto;
    private final SaleTransactionToSaleTransactionDto saleToSaleDto;

    public TransactionServiceImpl(TransactionViewRepository transactionViewRepository, PurchaseTransactionRepository purchaseTrRepository, SaleTransactionRepository saleTrRepository, PurchaseTransactionToPurchaseTransactionDto purchaseToPurchaseDto, SaleTransactionToSaleTransactionDto saleToSaleDto) {
        this.transactionViewRepository = transactionViewRepository;
        this.purchaseTrRepository = purchaseTrRepository;
        this.saleTrRepository = saleTrRepository;
        this.purchaseToPurchaseDto = purchaseToPurchaseDto;
        this.saleToSaleDto = saleToSaleDto;
    }


    @Override
    public Page<TransactionView> getTransactionPage(int page, int size, String sort, String search) {
        final Pageable pageable = PageRequest.of(page, size, Sort.by(sort != null ? sort : "id"));
        if(search == null || search.length() == 0) {
            return  this.transactionViewRepository.findAll(pageable);
        } else {
            return  this.transactionViewRepository.findAll(pageable, search);
        }
    }

    @Override
    public Page<SaleTransactionDto> getSaleTransactionPage(int page, int size, String sort, String search) {
        final Pageable pageable = PageRequest.of(page, size, Sort.by(sort != null ? sort : "id"));
        if(search == null || search.length() == 0) {
            return  this.saleTrRepository.findAll(pageable).map(saleToSaleDto::convert);
        } else {
            return  this.saleTrRepository.findAll(pageable, search).map(saleToSaleDto::convert);
        }

    }

    @Override
    public Page<PurchaseTransactionDto> getPurchaseTransaction(int page, int size, String sort, String search) {
        final Pageable pageable = PageRequest.of(page, size, Sort.by(sort != null ? sort : "id"));
        if (search == null || search.length() == 0) {
          return  this.purchaseTrRepository.findAll(pageable).map(purchaseToPurchaseDto::convert);
        } else {
            return  this.purchaseTrRepository.findAll(pageable, search).map(purchaseToPurchaseDto::convert);
        }
    }
}
