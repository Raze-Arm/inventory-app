package raze.spring.inventory.service;

import raze.spring.inventory.domain.dto.ProductDto;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    List<ProductDto> getProductList();
    ProductDto getProduct(UUID id);


    UUID saveProduct(ProductDto productDto);

    void updateProduct(ProductDto productDto);

    void deleteProduct(UUID id);


}
