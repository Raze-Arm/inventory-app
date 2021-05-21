package raze.spring.inventory.converter;


import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import raze.spring.inventory.domain.SaleInvoice;
import raze.spring.inventory.domain.dto.SaleInvoiceDto;
import raze.spring.inventory.utility.DateMapper;

import java.util.stream.Collectors;

@Component
public class SaleInvoiceToSaleInvoiceDto implements Converter<SaleInvoice, SaleInvoiceDto> {
    private final DateMapper dateMapper;
    private final SaleTransactionToSaleTransactionDto saleTransactionToSaleTransactionDto;
    private final CustomerToCustomerDto customerToCustomerDto;

    public SaleInvoiceToSaleInvoiceDto(DateMapper dateMapper, SaleTransactionToSaleTransactionDto saleTransactionToSaleTransactionDto, CustomerToCustomerDto customerToCustomerDto) {
        this.dateMapper = dateMapper;
        this.saleTransactionToSaleTransactionDto = saleTransactionToSaleTransactionDto;
        this.customerToCustomerDto = customerToCustomerDto;
    }

    @Synchronized
    @Override
    public SaleInvoiceDto convert(SaleInvoice saleInvoice) {
        return SaleInvoiceDto.builder()
            .id(saleInvoice.getId())
            .createdDate(dateMapper.asOffsetDateTime(saleInvoice.getCreatedDate()))
            .customer(this.customerToCustomerDto.convert(saleInvoice.getCustomer()))
            .transactions(
                saleInvoice.getTransactions().stream()
                    .map(this.saleTransactionToSaleTransactionDto::convert)
                    .collect(Collectors.toSet()))
            .build();
    }
}
