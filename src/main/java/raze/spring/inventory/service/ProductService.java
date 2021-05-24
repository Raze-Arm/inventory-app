package raze.spring.inventory.service;

import org.springframework.data.domain.Page;
import raze.spring.inventory.domain.dto.ProductDto;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    Page<ProductDto> getProductDtoPage(int page, int size, String sort, String search);


    List<ProductDto> getProductList();
    ProductDto getProduct(UUID id);


    UUID saveProduct(ProductDto productDto);

    void updateProduct(ProductDto productDto);

    void deleteProduct(UUID id);


}
