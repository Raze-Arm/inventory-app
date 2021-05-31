package raze.spring.inventory.converter;


import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import raze.spring.inventory.domain.PurchaseTransaction;
import raze.spring.inventory.domain.dto.PurchaseTransactionDto;
import raze.spring.inventory.repository.ProductRepository;

@Component
public class PurchaseTransactionDtoToPurchaseTransaction implements Converter<PurchaseTransactionDto, PurchaseTransaction> {
    private final ProductRepository productRepository;

    public PurchaseTransactionDtoToPurchaseTransaction(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Synchronized
    @Override
    public PurchaseTransaction convert(PurchaseTransactionDto purchaseTransactionDto) {
        return PurchaseTransaction.builder()
                .product(productRepository.findById(purchaseTransactionDto.getProductId()).orElse(null)) //TODO
                .price(purchaseTransactionDto.getPrice())
                .description(purchaseTransactionDto.getDescription())
                .quantity(purchaseTransactionDto.getQuantity())
                .build();
    }
}
