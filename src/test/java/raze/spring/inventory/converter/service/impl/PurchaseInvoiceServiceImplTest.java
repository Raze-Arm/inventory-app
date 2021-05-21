package raze.spring.inventory.converter.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import raze.spring.inventory.converter.service.PurchaseInvoiceService;
import raze.spring.inventory.domain.Product;
import raze.spring.inventory.domain.PurchaseInvoice;
import raze.spring.inventory.domain.PurchaseTransaction;
import raze.spring.inventory.domain.Supplier;
import raze.spring.inventory.domain.dto.PurchaseInvoiceDto;
import raze.spring.inventory.domain.dto.PurchaseTransactionDto;
import raze.spring.inventory.domain.dto.SupplierDto;
import raze.spring.inventory.repository.ProductRepository;
import raze.spring.inventory.repository.PurchaseInvoiceRepository;
import raze.spring.inventory.repository.PurchaseTransactionRepository;
import raze.spring.inventory.repository.SupplierRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Profile("test")
@ActiveProfiles("test")
@SpringBootTest()
class PurchaseInvoiceServiceImplTest {
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
    private static  String FIRST_NAME = "FIRST NAME";
    private static  String FIRST_NAME2 = "FIRST NAME2";
    private static String LAST_NAME = "LAST NAME";
    private static String LAST_NAME2 = "LAST NAME2";
    private static String ADDRESS = "ADDRESS";

    @Autowired
    private PurchaseInvoiceRepository invoiceRepository;

    @Autowired
    private PurchaseTransactionRepository transactionRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private PurchaseInvoiceService invoiceService;


    PurchaseTransaction transaction;
    PurchaseInvoice invoice;
    @BeforeEach
    public void beforeEachTest(){
        invoiceRepository.deleteAll();
        transactionRepository.deleteAll();

        final Product product1 = Product.builder().name(NAME).build();
        final Product product2 = Product.builder().name(NAME).build();
        this.productRepository.saveAll(Set.of(product1, product2));

        transaction = PurchaseTransaction.builder().product(product1).quantity(QUANTITY).build();


        final Supplier supplier = Supplier.builder().firstName(FIRST_NAME).build();
        supplierRepository.saveAndFlush(supplier);
        invoice =
            PurchaseInvoice.builder()
                .transactions(Set.of(transaction))
                .supplier(supplier)
                .build();

        invoiceRepository.save(invoice);
    }



    @Test
    void getInvoiceList() {

        List<PurchaseInvoiceDto> invoiceDtoList = invoiceService.getInvoiceList();
        assertEquals(invoiceDtoList.size(), 1);

    }

    @Test
    void getInvoice() {
        final PurchaseInvoiceDto invoiceDto = this.invoiceService.getInvoice(invoice.getId());
        assertEquals(invoiceDto.getTransactions().size() , 1);
        assertEquals(invoiceDto.getSupplier().getFirstName(), FIRST_NAME);
    }

    @Test
    void saveInvoice() {
        final Product product = Product.builder().name(NAME).build();
        this.productRepository.save(product);
        final Supplier supplier = Supplier.builder().firstName(FIRST_NAME).build();
        this.supplierRepository.save(supplier);
        final PurchaseTransactionDto transaction = PurchaseTransactionDto.builder().quantity(QUANTITY).productId(product.getId()).build();
        final PurchaseInvoiceDto invoice =
            PurchaseInvoiceDto.builder()
                .transactions(Set.of(transaction))
                .supplier(SupplierDto.builder().id(supplier.getId()).firstName(FIRST_NAME).build())
                    .build();
        final UUID id = this.invoiceService.saveInvoice(invoice);
        final PurchaseInvoice savedInvoice = this.invoiceRepository.findById(id).orElseThrow();

        assertNotNull(id);
        assertEquals(savedInvoice.getTransactions().size(), 1);
        assertEquals(savedInvoice.getSupplier().getFirstName(), FIRST_NAME);
    }

    @Test
    void deleteInvoice() {

        this.invoiceService.deleteInvoice(invoice.getId());

        final PurchaseInvoice purchaseInvoice = this.invoiceRepository.findById(invoice.getId()).orElse(null);

        assertNull(purchaseInvoice);
        assertEquals(this.transactionRepository.findAll().size(), 0);


    }
}