package raze.spring.inventory.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import raze.spring.inventory.domain.*;
import raze.spring.inventory.domain.dto.PurchaseTransactionDto;
import raze.spring.inventory.repository.*;
import raze.spring.inventory.service.PurchaseInvoiceService;
import raze.spring.inventory.service.SaleInvoiceService;
import raze.spring.inventory.domain.dto.CustomerDto;
import raze.spring.inventory.domain.dto.SaleInvoiceDto;
import raze.spring.inventory.domain.dto.SaleTransactionDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@Profile("test")
@ActiveProfiles({"test", "dev"})
@SpringBootTest()
class SaleInvoiceServiceImplTest {
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
    private SaleInvoiceRepository invoiceRepository;

    @Autowired
    private SaleTransactionRepository transactionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SaleInvoiceService invoiceService;

    @Autowired
    private PurchaseTransactionRepository purchaseTransactionRepository;

    SaleTransaction transaction;
    SaleInvoice invoice;
    @BeforeEach
    public void beforeEachTest(){
        invoiceRepository.deleteAll();
        transactionRepository.deleteAll();

        final Product product1 = Product.builder().name(NAME).build();
        final Product product2 = Product.builder().name(NAME).build();
        this.productRepository.saveAll(Set.of(product1, product2));

        transaction = SaleTransaction.builder().product(product1).quantity(QUANTITY).build();

        final Customer customer = Customer.builder().firstName(FIRST_NAME).build();
        customerRepository.saveAndFlush(customer);

        invoice = SaleInvoice.builder().transactions(Set.of(transaction)).customer(customer).build();

        invoiceRepository.save(invoice);

    }

    @Test
    void getInvoiceList() {
        List<SaleInvoiceDto> invoiceDtoList = invoiceService.getInvoiceList();
        assertEquals(invoiceDtoList.size(), 1);
    }

    @Test
    void getInvoice() {
        final SaleInvoiceDto invoiceDto = this.invoiceService.getInvoice(invoice.getId());
        assertEquals(invoiceDto.getTransactions().size(), 1);
        assertEquals(invoiceDto.getCustomer().getFirstName() , FIRST_NAME);
    }

    @Test
    void saveInvoice() {
        final Product product = Product.builder().name(NAME).build();
        this.productRepository.save(product);
        final Customer customer = Customer.builder().firstName(FIRST_NAME).build();
        this.customerRepository.save(customer);
        final PurchaseTransaction purchaseTransaction = PurchaseTransaction.builder().quantity(QUANTITY + 2) .product(product).build();

        final SaleTransactionDto  transactionDto = SaleTransactionDto.builder().quantity(QUANTITY).productId(product.getId()).build();
        final SaleInvoiceDto invoiceDto =
            SaleInvoiceDto.builder()
                .transactions(Set.of(transactionDto))
                .customer(CustomerDto.builder().id(customer.getId()).firstName(FIRST_NAME).build())
                .build();


        this.purchaseTransactionRepository.save(purchaseTransaction);
        final UUID id = this.invoiceService.saveInvoice(invoiceDto);
        final  SaleInvoice savedInvoice =  this.invoiceRepository.findById(id).orElseThrow();
        assertNotNull(id);
        assertEquals(savedInvoice.getTransactions().size(), 1);
        assertEquals(savedInvoice.getCustomer().getFirstName(), FIRST_NAME);

    }

    @Test
    void deleteInvoice() {
        this.invoiceService.deleteInvoice(invoice.getId());

        final SaleInvoice saleInvoice = this.invoiceRepository.findById(invoice.getId()).orElse(null);

        assertNull(saleInvoice);
        assertEquals(this.transactionRepository.findAll().size(), 0);

    }
}