package raze.spring.inventory.converter.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import raze.spring.inventory.converter.service.SupplierService;
import raze.spring.inventory.domain.Supplier;
import raze.spring.inventory.domain.dto.SupplierDto;
import raze.spring.inventory.repository.SupplierRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Profile("test")
@ActiveProfiles("test")
@SpringBootTest()
class SupplierServiceImplTest {

    private static  String FIRST_NAME = "FIRST NAME";
    private static  String FIRST_NAME2 = "FIRST NAME2";
    private static String LAST_NAME = "LAST NAME";
    private static String LAST_NAME2 = "LAST NAME2";
    private static String ADDRESS = "ADDRESS";

    @Autowired
    private SupplierService supplierService;
    @Autowired
    private SupplierRepository supplierRepository;


    @BeforeEach
    public void beforeEachTest(){
        this.supplierRepository.deleteAll();
    }


    @Test
    void getSupplierList() {
        final Supplier supplier1 = Supplier.builder().firstName(FIRST_NAME).build();
        final Supplier supplier2 = Supplier.builder().firstName(FIRST_NAME2).build();
        this.supplierRepository.saveAll(Set.of(supplier1, supplier2));


        List<SupplierDto> supplierDtos = this.supplierService.getSupplierList();
        assertEquals(supplierDtos.size(), 2);
    }

    @Test
    void getSupplier() {
        final Supplier supplier1 = Supplier.builder().firstName(FIRST_NAME).build();
        final Supplier supplier2 = Supplier.builder().firstName(FIRST_NAME2).build();

        this.supplierRepository.saveAll(Set.of(supplier1, supplier2));

        SupplierDto supplierDto = this.supplierService.getSupplier(supplier1.getId());
        assertEquals(supplierDto.getFirstName(), FIRST_NAME);
    }

    @Test
    void saveSupplier() {
        final SupplierDto supplierDto = SupplierDto.builder().firstName(FIRST_NAME).lastName(LAST_NAME).build();

        final UUID id  = this.supplierService.saveSupplier(supplierDto);
        final Supplier savedSupplier = this.supplierRepository.findById(id).orElseThrow();

        assertNotNull(id);
        assertEquals(savedSupplier.getFirstName(), FIRST_NAME);
        assertEquals(savedSupplier.getLastName(), LAST_NAME);
    }

    @Test
    void updateSupplier() {
        final Supplier supplier = Supplier.builder().firstName(FIRST_NAME).lastName(LAST_NAME).build();
         this.supplierRepository.save(supplier);

        final SupplierDto supplierDto =  SupplierDto.builder().id(supplier.getId()).firstName(FIRST_NAME2).lastName(LAST_NAME).address(ADDRESS).build();

        this.supplierService.updateSupplier(supplierDto);

        final Supplier updatedSupplier = this.supplierRepository.findById(supplier.getId()).orElseThrow();

        assertEquals(updatedSupplier.getFirstName() ,FIRST_NAME2);
        assertEquals(updatedSupplier.getLastName() ,LAST_NAME);
        assertEquals(updatedSupplier.getAddress() ,ADDRESS);

    }

    @Test
    void deleteSupplier() {
        final Supplier supplier = Supplier.builder().firstName(FIRST_NAME).lastName(LAST_NAME).build();
        this.supplierRepository.save(supplier);

        this.supplierService.deleteSupplier(supplier.getId());


        final Supplier sup =  this.supplierRepository.findById(supplier.getId()).orElse(null);

        assertNull(sup);

    }
}