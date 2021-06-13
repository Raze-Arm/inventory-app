package raze.spring.inventory.converter;

import lombok.SneakyThrows;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import raze.spring.inventory.Exception.ObjectNotFoundException;
import raze.spring.inventory.domain.Product;
import raze.spring.inventory.domain.SaleTransaction;
import raze.spring.inventory.domain.dto.SaleTransactionDto;
import raze.spring.inventory.domain.view.ProductView;
import raze.spring.inventory.repository.ProductRepository;
import raze.spring.inventory.repository.ProductViewRepository;

@Component
public class SaleTransactionDtoToSaleTransaction implements Converter<SaleTransactionDto , SaleTransaction> {
    private final ProductRepository productRepository;
    private final ProductViewRepository productViewRepository;

    public SaleTransactionDtoToSaleTransaction(ProductRepository productRepository, ProductViewRepository productViewRepository) {
        this.productRepository = productRepository;
        this.productViewRepository = productViewRepository;
    }

    @SneakyThrows
    @Synchronized
    @Override
    public SaleTransaction convert(SaleTransactionDto saleTransactionDto) {
        final Product product = this.productRepository.findById(saleTransactionDto.getProductId()).orElseThrow(); //TODO
        final ProductView productView = this.productViewRepository.findById(saleTransactionDto.getProductId()).orElseThrow(); //TODO
        if(saleTransactionDto.getQuantity() > productView.getQuantity()) throw new ObjectNotFoundException("Requested quantity is higher than available  products in inventory");
        return SaleTransaction.builder()
                .product(product)
                .price(saleTransactionDto.getPrice())
                .quantity(saleTransactionDto.getQuantity())
                .description(saleTransactionDto.getDescription())
                .build();
    }
}
