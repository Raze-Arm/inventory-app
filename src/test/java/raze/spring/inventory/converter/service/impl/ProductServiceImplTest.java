package raze.spring.inventory.converter.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import raze.spring.inventory.converter.service.ProductService;
import raze.spring.inventory.domain.Product;
import raze.spring.inventory.domain.PurchaseTransaction;
import raze.spring.inventory.domain.dto.ProductDto;
import raze.spring.inventory.domain.view.ProductView;
import raze.spring.inventory.repository.ProductRepository;
import raze.spring.inventory.repository.ProductViewRepository;
import raze.spring.inventory.repository.PurchaseTransactionRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Profile("test")
@ActiveProfiles("test")
@SpringBootTest()
class ProductServiceImplTest {
    private static UUID ID = UUID.randomUUID();
    private static UUID ID2 = UUID.randomUUID();
    private static String NAME = "NAME";
    private static String NAME2 = "NAME2";
    private static BigDecimal PRICE = new BigDecimal("100000");
    private static BigDecimal PRICE2 = new BigDecimal("222222");
    private static BigDecimal SALE_PRICE = new BigDecimal("3333333");
    private static BigDecimal SALE_PRICE2 = new BigDecimal("4444444");
    private static Long QUANTITY = 5L;
    private static Long QUANTITY2 = 7L;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;


    @Autowired
    private ProductViewRepository productViewRepository;


    @BeforeEach
    public void beforeEachTest(){
        final ProductView productView1 = ProductView.builder().id(ID).name(NAME).price(PRICE).salePrice(SALE_PRICE).quantity(QUANTITY).build();
        final ProductView productView2 = ProductView.builder().id(ID2).name(NAME2).price(PRICE2).salePrice(SALE_PRICE2).quantity(QUANTITY2).build();
        productViewRepository.saveAll(Set.of(productView1, productView2));
    }

    @AfterEach
    public void afterEachTest() {
        productViewRepository.deleteAll();
    }


    @Test
    void getProductList() {

        List<ProductDto> productDtos = this.productService.getProductList();
        assertEquals(productDtos.size(), 2);
    }

    @Test
    void getProduct() {

        final ProductDto productDto = this.productService.getProduct(ID);
        assertEquals(productDto.getName(), NAME);
        assertEquals(productDto.getQuantity(), QUANTITY);
        assertEquals(productDto.getPrice().stripTrailingZeros(), PRICE.stripTrailingZeros());
        assertEquals(productDto.getSalePrice().stripTrailingZeros() , SALE_PRICE.stripTrailingZeros());
    }

    @Test
    void saveProduct() {
        final ProductDto productDto = ProductDto.builder().name(NAME).price(PRICE).salePrice(SALE_PRICE).build();

        final UUID id = this.productService.saveProduct(productDto);
        final Product savedProduct = this.productRepository.findById(id).orElseThrow();

        assertNotNull(id);
        assertEquals(savedProduct.getName(), NAME);
        assertEquals(savedProduct.getPrice().stripTrailingZeros(), PRICE.stripTrailingZeros());
        assertEquals(savedProduct.getSalePrice().stripTrailingZeros(), SALE_PRICE.stripTrailingZeros());
    }

    @Test
    void updateProduct() {
        final Product product = Product.builder().name(NAME).price(PRICE).salePrice(SALE_PRICE).build();
        this.productRepository.save(product);

        final ProductDto productDto = ProductDto.builder().id(product.getId()).name(NAME2).price(PRICE2).salePrice(SALE_PRICE2).build();

        this.productService.updateProduct(productDto);

        final Product updatedProduct = this.productRepository.findById(product.getId()).orElseThrow();

        assertEquals(updatedProduct.getName(), NAME2);
        assertEquals(updatedProduct.getPrice().stripTrailingZeros(), PRICE2.stripTrailingZeros());
        assertEquals(updatedProduct.getSalePrice().stripTrailingZeros(), SALE_PRICE2.stripTrailingZeros());

    }

    @Test
    void deleteProduct() {
        final Product product = Product.builder().name(NAME).price(PRICE).salePrice(SALE_PRICE).build();
        this.productRepository.save(product);


        this.productService.deleteProduct(product.getId());
        final Product prod =  this.productRepository.findById(product.getId()).orElse(null);

        assertNull(prod);
    }
}