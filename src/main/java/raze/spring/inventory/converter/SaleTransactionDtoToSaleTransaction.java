package raze.spring.inventory.converter;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import raze.spring.inventory.domain.Product;
import raze.spring.inventory.domain.SaleTransaction;
import raze.spring.inventory.domain.dto.SaleTransactionDto;
import raze.spring.inventory.repository.ProductRepository;

@Component
public class SaleTransactionDtoToSaleTransaction implements Converter<SaleTransactionDto , SaleTransaction> {
    private final ProductRepository productRepository;

    public SaleTransactionDtoToSaleTransaction(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Synchronized
    @Override
    public SaleTransaction convert(SaleTransactionDto saleTransactionDto) {
        final Product product = this.productRepository.findById(saleTransactionDto.getProductId()).orElse(null); //TODO
        return SaleTransaction.builder()
                .product(product)
                .price(saleTransactionDto.getPrice())
                .quantity(saleTransactionDto.getQuantity())
                .description(saleTransactionDto.getDescription())
                .build();
    }
}
