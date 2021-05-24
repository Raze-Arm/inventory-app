package raze.spring.inventory.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raze.spring.inventory.service.CustomerService;
import raze.spring.inventory.domain.dto.CustomerDto;

import java.util.List;
import java.util.UUID;

@RestController
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping(path = {"/customer", "/customer/"})
    public ResponseEntity<Page<CustomerDto>> getCustomerPage(
        @RequestParam("page") Integer page,
        @RequestParam("size") Integer size,
        @RequestParam(value = "sort",required = false) String sort,
        @RequestParam(value = "search", required = false) String search) {
        return ResponseEntity.ok(this.customerService.getCustomerPage(page,size,sort, search));
    }

    @GetMapping(path = {"/customer", "/customer/"}, params = {"search-type=list"})
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        return  ResponseEntity.ok(this.customerService.getCustomerList());
    }

    @GetMapping(path = {"/customer/{id}", "/customer/{id}/"})
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable("id")UUID id) {
        return ResponseEntity.ok(this.customerService.getCustomer(id));
    }

    @PostMapping(path = {"/customer", "/customer/"})
    public ResponseEntity<UUID> saveCustomer(@RequestBody CustomerDto customerDto) {
        return ResponseEntity.ok(this.customerService.saveCustomer(customerDto));
    }

    @PutMapping(path = {"/customer", "/customer/"} )
    public ResponseEntity<Void> updateCustomer(@RequestBody CustomerDto customerDto) {
        this.customerService.updateCustomer(customerDto);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping(path = {"/customer/{id}", "/customer/{id}/"})
    public ResponseEntity<Void> deleteCustomer(@PathVariable UUID id) {
        this.customerService.deleteCustomer(id);
        return ResponseEntity.ok().build();
    }

}
