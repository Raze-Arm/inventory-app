package raze.spring.inventory.converter.service.impl;

import org.springframework.stereotype.Service;
import raze.spring.inventory.converter.ProductDtoToProduct;
import raze.spring.inventory.converter.ProductViewToProductDto;
import raze.spring.inventory.converter.service.ProductService;
import raze.spring.inventory.domain.Product;
import raze.spring.inventory.domain.dto.ProductDto;
import raze.spring.inventory.repository.ProductRepository;
import raze.spring.inventory.repository.ProductViewRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductViewRepository productViewRepository;
    private final ProductDtoToProduct productDtoToProduct;
    private final ProductViewToProductDto productViewToProductDto;

      public ProductServiceImpl(
          ProductRepository productRepository,
          ProductViewRepository productViewRepository,
          ProductDtoToProduct productDtoToProduct,
          ProductViewToProductDto productViewToProductDto) {
        this.productRepository = productRepository;
        this.productViewRepository = productViewRepository;
        this.productDtoToProduct = productDtoToProduct;
        this.productViewToProductDto = productViewToProductDto;
      }
    @Transactional
    @Override
    public List<ProductDto> getProductList() {

        return this.productViewRepository.findAll().stream()
            .map(this.productViewToProductDto::convert)
            .collect(Collectors.toList());
    }
    @Transactional
    @Override
    public ProductDto getProduct(UUID id) {

        return this.productViewToProductDto.convert(
            this.productViewRepository.findById(id).orElseThrow());
    }
    @Transactional
    @Override
    public UUID saveProduct(ProductDto productDto) {
        final Product product =
            this.productRepository.save(
                Objects.requireNonNull(this.productDtoToProduct.convert(productDto)));
        return product.getId();
    }

    @Transactional
    @Override
    public void updateProduct(ProductDto productDto) {
            if(productDto.getId() == null) throw new NoSuchElementException();
            final Product productToEdit= this.productRepository.findById(productDto.getId()).orElseThrow();
            productToEdit.setName(productDto.getName());
            productToEdit.setPrice(productDto.getPrice());
            productToEdit.setSalePrice(productDto.getSalePrice());
            productToEdit.setDescription(productDto.getDescription());

            this.productRepository.save(productToEdit);
    }

    @Override
    public void deleteProduct(UUID id) {
        this.productRepository.deleteById(id);
    }
}
