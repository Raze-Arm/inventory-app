package raze.spring.inventory.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import raze.spring.inventory.service.CustomerService;
import raze.spring.inventory.domain.Customer;
import raze.spring.inventory.domain.dto.CustomerDto;
import raze.spring.inventory.repository.CustomerRepository;


import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Profile("test")
@ActiveProfiles({"test", "dev", "mail"})
@SpringBootTest
class CustomerServiceImplTest {
    private static  String FIRST_NAME = "FIRST NAME";
    private static  String FIRST_NAME2 = "FIRST NAME2";
    private static String LAST_NAME = "LAST NAME";
    private static String LAST_NAME2 = "LAST NAME2";
    private static String ADDRESS = "ADDRESS";

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    public void beforeEachTest(){
        this.customerRepository.deleteAll();
    }

    @Test
    void getCustomerList() {
        final Customer customer1 = Customer.builder().firstName(FIRST_NAME).lastName(LAST_NAME).build();
        final Customer customer2 = Customer.builder().firstName(FIRST_NAME2).lastName(LAST_NAME2).build();
        this.customerRepository.saveAll(Set.of(customer1, customer2));


        List<CustomerDto> customerList = this.customerService.getCustomerList();
        assertEquals(customerList.size() , 2);
    }

    @Test
    void getCustomer() {
        final Customer customer1 = Customer.builder().firstName(FIRST_NAME).lastName(LAST_NAME).build();
        final Customer customer2 = Customer.builder().firstName(FIRST_NAME2).lastName(LAST_NAME2).build();
        this.customerRepository.saveAll(Set.of(customer1, customer2));

        CustomerDto customerDto = this.customerService.getCustomer(customer1.getId());
        assertEquals(customerDto.getFirstName(), FIRST_NAME);
    }

    @Test
    void saveCustomer() {
        final CustomerDto customerDto  = CustomerDto.builder().firstName(FIRST_NAME).lastName(LAST_NAME).build();

        final UUID id = this.customerService.saveCustomer(customerDto);
        final Customer customer = this.customerRepository.findById(id).orElseThrow();

        assertNotEquals(id, null);
        assertEquals(customer.getFirstName(), FIRST_NAME);
        assertEquals(customer.getLastName(), LAST_NAME);


    }

    @Test
    void updateCustomer() {
        final Customer customer = Customer.builder().firstName(FIRST_NAME).lastName(LAST_NAME).build();
         this.customerRepository.save(customer);


        final CustomerDto customerDto = CustomerDto.builder().id(customer.getId()).firstName(FIRST_NAME2).lastName(LAST_NAME).address(ADDRESS).build();

        this.customerService.updateCustomer(customerDto);

        final Customer updatedCustomer = this.customerRepository.findById(customer.getId()).orElseThrow();

        assertEquals(updatedCustomer.getFirstName(), FIRST_NAME2);
        assertEquals(updatedCustomer.getLastName(), LAST_NAME);
        assertEquals(updatedCustomer.getAddress(), ADDRESS);

    }

    @Test
    void deleteCustomer() {
        final Customer customer = Customer.builder().firstName(FIRST_NAME).lastName(LAST_NAME).build();
         this.customerRepository.save(customer);

        this.customerService.deleteCustomer(customer.getId());

        final Customer cust = this.customerRepository.findById(customer.getId()).orElse(null);

        assertNull(cust);

    }
}