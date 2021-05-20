package raze.spring.inventory.converter;


import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import raze.spring.inventory.domain.dto.ProductDto;
import raze.spring.inventory.domain.view.ProductView;
import raze.spring.inventory.utility.DateMapper;


@Component
public class ProductViewToProductDto implements Converter<ProductView, ProductDto> {

    private final DateMapper dateMapper;

    public ProductViewToProductDto(DateMapper dateMapper) {
        this.dateMapper = dateMapper;
    }

    @Synchronized
    @Override
    public ProductDto convert(ProductView product) {
        return ProductDto.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .price(product.getPrice())
                    .salePrice(product.getSalePrice())
                    .quantity(product.getQuantity())
                    .description(product.getDescription())
                    .createdDate(dateMapper.asOffsetDateTime(product.getCreatedDate()))
                .build();
    }
}
