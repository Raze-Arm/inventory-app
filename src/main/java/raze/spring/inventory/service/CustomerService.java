package raze.spring.inventory.service;

import raze.spring.inventory.domain.dto.CustomerDto;

import java.util.List;
import java.util.UUID;

public interface CustomerService {


    List<CustomerDto> getCustomerList();
    CustomerDto getCustomer(UUID id);

    UUID saveCustomer(CustomerDto customerDto);
    void updateCustomer(CustomerDto customerDto);

    void deleteCustomer(UUID id);
}
