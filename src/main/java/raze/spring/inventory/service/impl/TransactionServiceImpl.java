package raze.spring.inventory.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import raze.spring.inventory.domain.view.TransactionView;
import raze.spring.inventory.repository.TransactionViewRepository;
import raze.spring.inventory.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionViewRepository transactionViewRepository;

    public TransactionServiceImpl(TransactionViewRepository transactionViewRepository) {
        this.transactionViewRepository = transactionViewRepository;
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
}
