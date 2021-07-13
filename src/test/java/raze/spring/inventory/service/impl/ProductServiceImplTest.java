package raze.spring.inventory.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import raze.spring.inventory.domain.PurchaseTransaction;
import raze.spring.inventory.repository.PurchaseTransactionRepository;
import raze.spring.inventory.service.ProductService;
import raze.spring.inventory.domain.Product;
import raze.spring.inventory.domain.dto.ProductDto;
import raze.spring.inventory.domain.view.ProductView;
import raze.spring.inventory.repository.ProductRepository;
import raze.spring.inventory.repository.ProductViewRepository;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Profile("test")
@ActiveProfiles({"test", "dev", "mail"})
@SpringBootTest()
class ProductServiceImplTest {
    private static UUID ID = UUID.randomUUID();
    private static UUID ID2 = UUID.randomUUID();
    private static String NAME = "NAME";
    private static String NAME2 = "NAME2";
    private static BigInteger PRICE = new BigInteger("100000");
    private static BigInteger PRICE2 = new BigInteger("222222");
    private static BigInteger SALE_PRICE = new BigInteger("3333333");
    private static BigInteger SALE_PRICE2 = new BigInteger("4444444");
    private static Long QUANTITY = 5L;
    private static Long QUANTITY2 = 7L;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PurchaseTransactionRepository transactionRepository;


    @Autowired
    private ProductViewRepository productViewRepository;


    @BeforeEach
    public void beforeEachTest(){
        this.transactionRepository.deleteAll();
        this.productRepository.deleteAll();
    }


    @Test
    void getProductList() {
        final Product product = Product.builder().name(NAME).price(PRICE).salePrice(SALE_PRICE).build();
        this.productRepository.save(product);
        final Product product2 = Product.builder().name(NAME2).price(PRICE2).salePrice(SALE_PRICE2).build();
        this.productRepository.save(product2);
        final PurchaseTransaction transaction = PurchaseTransaction.builder().product(product).quantity(QUANTITY).build();
        final PurchaseTransaction transaction2 = PurchaseTransaction.builder().product(product2).quantity(QUANTITY).build();
        this.transactionRepository.save(transaction);
        this.transactionRepository.save(transaction2);

        List<ProductDto> productDtos = this.productService.getProductList();
        assertEquals(productDtos.size(), 2);
    }

    @Test
    void getProduct() {
        final Product product = Product.builder().name(NAME).price(PRICE).salePrice(SALE_PRICE).build();
        this.productRepository.save(product);
        final PurchaseTransaction transaction = PurchaseTransaction.builder().product(product).quantity(QUANTITY).build();
        this.transactionRepository.save(transaction);


        final ProductDto productDto = this.productService.getProduct(product.getId());
        assertEquals(productDto.getName(), NAME);
        assertEquals(productDto.getQuantity(), QUANTITY);
        assertEquals(productDto.getPrice(), PRICE);
        assertEquals(productDto.getSalePrice() , SALE_PRICE);
    }

    @Test
    void saveProduct() throws IOException {
        final ProductDto productDto = ProductDto.builder().name(NAME).price(PRICE).salePrice(SALE_PRICE).build();

        final UUID id = this.productService.saveProduct(productDto);
        final Product savedProduct = this.productRepository.findById(id).orElseThrow();

        assertNotNull(id);
        assertEquals(savedProduct.getName(), NAME);
        assertEquals(savedProduct.getPrice(), PRICE);
        assertEquals(savedProduct.getSalePrice(), SALE_PRICE);
    }

    @Test
    void updateProduct() throws IOException {
        final Product product = Product.builder().name(NAME).price(PRICE).salePrice(SALE_PRICE).build();
        this.productRepository.save(product);

        final ProductDto productDto = ProductDto.builder().id(product.getId()).name(NAME2).price(PRICE2).salePrice(SALE_PRICE2).build();

        this.productService.updateProduct(productDto);

        final Product updatedProduct = this.productRepository.findById(product.getId()).orElseThrow();

        assertEquals(updatedProduct.getName(), NAME2);
        assertEquals(updatedProduct.getPrice(), PRICE2);
        assertEquals(updatedProduct.getSalePrice(), SALE_PRICE2);

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