package raze.spring.inventory.converter;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import raze.spring.inventory.domain.Customer;
import raze.spring.inventory.domain.SaleInvoice;
import raze.spring.inventory.domain.dto.SaleInvoiceDto;
import raze.spring.inventory.repository.CustomerRepository;

import java.util.stream.Collectors;

@Component
public class SaleInvoiceDtoToSaleInvoice implements Converter<SaleInvoiceDto , SaleInvoice> {
    private final SaleTransactionDtoToSaleTransaction saleTransactionDtoToSaleTransaction;
    private final CustomerRepository customerRepository;
    private final CustomerDtoToCustomer customerDtoToCustomer;

    public SaleInvoiceDtoToSaleInvoice(SaleTransactionDtoToSaleTransaction saleTransactionDtoToSaleTransaction, CustomerRepository customerRepository, CustomerDtoToCustomer customerDtoToCustomer) {
        this.saleTransactionDtoToSaleTransaction = saleTransactionDtoToSaleTransaction;
        this.customerRepository = customerRepository;
        this.customerDtoToCustomer = customerDtoToCustomer;
    }

    @Synchronized
    @Override
    public SaleInvoice convert(SaleInvoiceDto saleInvoiceDto) {
        Customer customer = null;
        if(saleInvoiceDto.getCustomer() != null)
            customer = this.customerRepository.findById(saleInvoiceDto.getCustomer().getId()).orElse(this.customerDtoToCustomer.convert(saleInvoiceDto.getCustomer()));
        return SaleInvoice.builder()
            .customer(customer)
            .transactions(
                saleInvoiceDto.getTransactions().stream()
                    .map(this.saleTransactionDtoToSaleTransaction::convert)
                    .collect(Collectors.toSet()))
            .build();
    }
}
