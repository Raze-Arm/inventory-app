package raze.spring.inventory.converter;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import raze.spring.inventory.domain.Product;
import raze.spring.inventory.domain.SaleTransaction;
import raze.spring.inventory.domain.dto.SaleTransactionDto;
import raze.spring.inventory.utility.DateMapper;

@Component
public class SaleTransactionToSaleTransactionDto implements Converter<SaleTransaction, SaleTransactionDto> {
    private final DateMapper dateMapper;

    public SaleTransactionToSaleTransactionDto(DateMapper dateMapper) {
        this.dateMapper = dateMapper;
    }

    @Synchronized
    @Override
    public SaleTransactionDto convert(SaleTransaction saleTransaction) {
        final Product product = saleTransaction.getProduct();
        return SaleTransactionDto.builder()
                .id(saleTransaction.getId())
                .productId(product != null ? product.getId() : null)
                .productName(saleTransaction.getProductName())
                .imageAvailable(product != null ? product.getImageAvailable() : null)
                .price(saleTransaction.getPrice())
                .quantity(saleTransaction.getQuantity())
                .description(saleTransaction.getDescription())
                .createdDate(dateMapper.asOffsetDateTime(saleTransaction.getCreatedDate()))
                .build();
    }
}
