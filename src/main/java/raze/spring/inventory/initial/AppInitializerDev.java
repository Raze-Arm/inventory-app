package raze.spring.inventory.initial;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.boot.CommandLineRunner;

import org.springframework.context.annotation.Profile;

import org.springframework.stereotype.Component;
import raze.spring.inventory.domain.*;
import raze.spring.inventory.domain.dto.ProfileDto;
import raze.spring.inventory.repository.*;
import raze.spring.inventory.security.model.UserAccount;
import raze.spring.inventory.security.repository.UserAccountRepository;
import raze.spring.inventory.security.role.UserRole;
import raze.spring.inventory.service.ProfileService;
import raze.spring.inventory.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;
import java.io.File;
import java.math.BigInteger;
import java.util.Set;
import java.util.UUID;


@Profile("!test & dev")
@Slf4j
@Component
public class AppInitializerDev implements CommandLineRunner {



    private final CustomerRepository customerRepository;
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;
    private final PurchaseTransactionRepository purchaseTransactionRepository;
    private final SaleTransactionRepository saleTransactionRepository;
    private final PurchaseInvoiceRepository purchaseInvoiceRepository;
    private final SaleInvoiceRepository saleInvoiceRepository;
    private final ProfileService profileService;
    private final UserService userService;
    private final UserProfileRepository profileRepository;
    private final UserAccountRepository accountRepository;




      public AppInitializerDev(
              CustomerRepository customerRepository,
              SupplierRepository supplierRepository,
              ProductRepository productRepository,
              PurchaseTransactionRepository purchaseTransactionRepository,
              SaleTransactionRepository saleTransactionRepository,
              PurchaseInvoiceRepository purchaseInvoiceRepository,
              SaleInvoiceRepository saleInvoiceRepository, ProfileService profileService, UserService userService, UserProfileRepository profileRepository, UserAccountRepository accountRepository) {
        this.customerRepository = customerRepository;
        this.supplierRepository = supplierRepository;
        this.productRepository = productRepository;
        this.purchaseTransactionRepository = purchaseTransactionRepository;
        this.saleTransactionRepository = saleTransactionRepository;
        this.purchaseInvoiceRepository = purchaseInvoiceRepository;
        this.saleInvoiceRepository = saleInvoiceRepository;
          this.profileService = profileService;
          this.userService = userService;
          this.profileRepository = profileRepository;
          this.accountRepository = accountRepository;
      }

      public  void cleanup() {
          this.customerRepository.deleteAll();
          this.supplierRepository.deleteAll();
          this.productRepository.deleteAll();
          this.purchaseInvoiceRepository.deleteAll();
          this.saleInvoiceRepository.deleteAll();
          this.profileRepository.deleteAll();
          this.accountRepository.deleteAll();
      }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
          cleanup();
        log.debug("Initializing Mock Data...");
        Supplier adibSupplier = Supplier.builder().id(UUID.randomUUID()).firstName("sadegh").lastName("adib").address("Esfehan").build();
        Supplier zareiSupplier = Supplier.builder().firstName("mohammad").lastName("zarei").address("Shiraz").build();

        adibSupplier = supplierRepository.save(adibSupplier);
        zareiSupplier = supplierRepository.save(zareiSupplier);

        Customer arianCustomer = Customer.builder().firstName("reza").lastName("arian").address("bushehr , kouche nafti").build();
        Customer palizanCustomer = Customer.builder().firstName("mostafa").lastName("palizan").address("bushehr , Ashouri").build();
        Customer sharifianCustomer = Customer.builder().firstName("masoud").lastName("sharifian").address("bushehr , Bahmani").build();

        arianCustomer = customerRepository.save(arianCustomer);
        palizanCustomer = customerRepository.save(palizanCustomer);
        sharifianCustomer = customerRepository.save(sharifianCustomer);


        customerRepository.saveAll(Set.of(arianCustomer, palizanCustomer, sharifianCustomer));


        Product backLight32Lb = Product.builder().name("32Lb LG").price(new BigInteger("3500000")) .salePrice(new BigInteger("4000000")).build();
        Product backLight40F5000 = Product.builder().name("40F 5000").price(new BigInteger("6000000")) .salePrice(new BigInteger("6500000")).build();
        Product backLight46F5000 = Product.builder().name("46F 5000").price(new BigInteger("7200000")) .salePrice(new BigInteger("8000000")).build();


        Product backLight32LN = Product.builder().name("32LN").price(new BigInteger("2800000")) .salePrice(new BigInteger("3500000")).build();



        productRepository.saveAll(Set.of(backLight32Lb,backLight32LN, backLight40F5000, backLight46F5000));


        PurchaseTransaction transaction1 = PurchaseTransaction.builder().product(backLight32Lb).description("adib supply").quantity(10L).price(new BigInteger("3500000")).build();
        PurchaseTransaction transaction2 = PurchaseTransaction.builder().product(backLight40F5000).description("adib supply").quantity(5L).price(new BigInteger("6000000")).build();
        PurchaseTransaction transaction3 = PurchaseTransaction.builder().product(backLight46F5000).description("adib supply").quantity(5L).price(new BigInteger("7200000")).build();
        PurchaseTransaction transaction4 = PurchaseTransaction.builder().product(backLight32LN).description("adib supply").quantity(10L).price(new BigInteger("2800000")).build();

        PurchaseTransaction transaction5 = PurchaseTransaction.builder().product(backLight32Lb).description("zarei supply").quantity(10L).price(new BigInteger("27000000")).build();
        PurchaseTransaction transaction6 = PurchaseTransaction.builder().product(backLight40F5000).description("zarei supply").quantity(10L).price(new BigInteger("6200000")).build();
        PurchaseTransaction transaction7 = PurchaseTransaction.builder().product(backLight46F5000).description("zarei supply").quantity(10L).price(new BigInteger("7000000")).build();
        PurchaseTransaction transaction8 = PurchaseTransaction.builder().product(backLight32LN).description("zarei supply").quantity(10L).price(new BigInteger("2500000")).build();



        SaleTransaction transaction9 = SaleTransaction.builder().product(backLight32Lb).description("arian sale").quantity(2L).price(new BigInteger("4000000")).build();
        SaleTransaction transaction10 = SaleTransaction.builder().product(backLight40F5000).description("arian sale").quantity(1L).price(new BigInteger("6500000")).build();
        SaleTransaction transaction11 = SaleTransaction.builder().product(backLight40F5000).description("arian sale").quantity(1L).price(new BigInteger("6500000")).build();
        SaleTransaction transaction12 = SaleTransaction.builder().product(backLight46F5000).description("arian sale").quantity(1L).price(new BigInteger("8000000")).build();

        SaleTransaction transaction13 = SaleTransaction.builder().product(backLight32Lb).description("palizan sale").quantity(1L).price(new BigInteger("4000000")).build();
        SaleTransaction transaction14 = SaleTransaction.builder().product(backLight46F5000).description("palizan sale").quantity(2L).price(new BigInteger("8000000")).build();

        SaleTransaction transaction15 = SaleTransaction.builder().product(backLight40F5000).description("sharifian sale").quantity(2L).price(new BigInteger("7000000")).build();
        SaleTransaction transaction16 = SaleTransaction.builder().product(backLight46F5000).description("sharifian sale").quantity(3L).price(new BigInteger("8000000")).build();


        Set<PurchaseTransaction> supplyList1 = Set.of(transaction1,  transaction3);
        Set<PurchaseTransaction> supplyList2 = Set.of(transaction2,  transaction4);
        Set<PurchaseTransaction> supplyList3 = Set.of(transaction4,  transaction5);


        Set<PurchaseTransaction> supplyList4 = Set.of(transaction6,  transaction7, transaction8);




        Set<SaleTransaction> saleList1 = Set.of(transaction9 , transaction10 ,transaction11);
        Set<SaleTransaction> saleList2 = Set.of(transaction12);
        Set<SaleTransaction> saleList3 = Set.of(transaction13);
        Set<SaleTransaction> saleList4 = Set.of(transaction14);
        Set<SaleTransaction> saleList5 = Set.of(transaction15,transaction16);




        PurchaseInvoice adibInvoice1 = PurchaseInvoice.builder().supplier(adibSupplier).transactions(supplyList1).build();
        PurchaseInvoice adibInvoice2 = PurchaseInvoice.builder().supplier(adibSupplier).transactions(supplyList2).build();
        PurchaseInvoice zareiInvoice1 = PurchaseInvoice.builder().supplier(zareiSupplier).transactions(supplyList3).build();
        PurchaseInvoice zareiInvoice2 = PurchaseInvoice.builder().supplier(zareiSupplier).transactions(supplyList4).build();

        SaleInvoice arianInvoice1 = SaleInvoice.builder().customer(arianCustomer).transactions(saleList1).build();
        SaleInvoice arianInvoice2 = SaleInvoice.builder().customer(arianCustomer).transactions(saleList2).build();
        SaleInvoice palizanInvoice1 = SaleInvoice.builder().customer(palizanCustomer).transactions(saleList3).build();
        SaleInvoice palizanInvoice2 = SaleInvoice.builder().customer(palizanCustomer).transactions(saleList4).build();
        SaleInvoice sharifianInvoice1 = SaleInvoice.builder().customer(sharifianCustomer).transactions(saleList5).build();

        FileUtils.cleanDirectory(new File("files/images/"));
        purchaseInvoiceRepository.saveAll(
            Set.of(adibInvoice1, adibInvoice2, zareiInvoice1, zareiInvoice2));

        saleInvoiceRepository.saveAll(
            Set.of(arianInvoice1, arianInvoice2, palizanInvoice1, palizanInvoice2, sharifianInvoice1));

        final ProfileDto profileDto =
            ProfileDto.builder()
                .firstName("raze")
                .lastName("arm")
                .username("admin")
                    .email("razear007@gmail.com")
                .password("12345")
                    .role(UserRole.ADMIN)
                .build();
        final ProfileDto profileDto1 =
                ProfileDto.builder()
                        .firstName("anna")
                        .lastName("noris")
                        .username("anna")
                        .email("razear007@gmail.com")
                        .password("12345")
                        .role(UserRole.BASIC)
                        .build();
        final ProfileDto profileDto2 =
                ProfileDto.builder()
                        .firstName("sam")
                        .lastName("vin")
                        .username("sam")
                        .email("sam@gmail.com")
                        .password("12345")
                        .role(UserRole.USER)
                        .build();
        userService.saveUser(profileDto);
        userService.saveUser(profileDto1);
        userService.saveUser(profileDto2);


    }
}
