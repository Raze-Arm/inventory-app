package raze.spring.inventory.service;

import org.springframework.data.domain.Page;
import raze.spring.inventory.domain.view.TransactionView;

public interface TransactionService {
    Page<TransactionView> getTransactionPage(int page, int size, String sort, String search);
}
