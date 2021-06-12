package raze.spring.inventory.service;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import raze.spring.inventory.domain.dto.ProductDto;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.UUID;

public interface ProductService {
    Page<ProductDto> getProductDtoPage(int page, int size, String sort, String search);


    List<ProductDto> getProductList();
    ProductDto getProduct(UUID id);

    Resource getProductImage(UUID id) ;
    Resource getProductSmallImage(UUID id);
    UUID saveProduct(ProductDto productDto) throws IOException;

    void updateProduct(ProductDto productDto) throws IOException;

    void deleteProduct(UUID id);


}
