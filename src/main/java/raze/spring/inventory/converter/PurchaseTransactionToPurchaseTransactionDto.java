package raze.spring.inventory.converter;


import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import raze.spring.inventory.domain.Product;
import raze.spring.inventory.domain.PurchaseTransaction;
import raze.spring.inventory.domain.dto.PurchaseTransactionDto;
import raze.spring.inventory.utility.DateMapper;

@Component
public class PurchaseTransactionToPurchaseTransactionDto implements Converter<PurchaseTransaction, PurchaseTransactionDto> {
    private final DateMapper dateMapper;

    public PurchaseTransactionToPurchaseTransactionDto(DateMapper dateMapper) {
        this.dateMapper = dateMapper;
    }

    @Synchronized
    @Override
    public PurchaseTransactionDto convert(PurchaseTransaction purchaseTransaction) {
        final Product product  = purchaseTransaction.getProduct();
        return PurchaseTransactionDto.builder()
                .id(purchaseTransaction.getId())
                .productId(product != null ? product.getId() : null)
                .productName(purchaseTransaction.getProductName())
                .price(purchaseTransaction.getPrice())
                .description(purchaseTransaction.getDescription())
                .quantity(purchaseTransaction.getQuantity())
                .createdDate(dateMapper.asOffsetDateTime(purchaseTransaction.getCreatedDate()))
                .build();
    }
}
