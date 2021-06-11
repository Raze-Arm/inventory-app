package raze.spring.inventory.converter;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import raze.spring.inventory.domain.Product;
import raze.spring.inventory.domain.dto.ProductDto;

@Component
public class ProductDtoToProduct implements Converter<ProductDto, Product> {
    @Synchronized
    @Override
    public Product convert(ProductDto productDto) {
        final Product product =  Product.builder()
                .name(productDto.getName())
                .price(productDto.getPrice())
                .salePrice(productDto.getSalePrice())
                .description(productDto.getDescription())
                .build();
        if(productDto.getImage() != null) product.setImageAvailable(true);
        return  product;
    }
}
