package raze.spring.inventory.service;

import org.springframework.data.domain.Page;
import raze.spring.inventory.domain.dto.CustomerDto;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    Page<CustomerDto> getCustomerPage(int page, int size, String sort, String search);

    List<CustomerDto> getCustomerList();
    CustomerDto getCustomer(UUID id);

    UUID saveCustomer(CustomerDto customerDto);
    void updateCustomer(CustomerDto customerDto);

    void deleteCustomer(UUID id);
}
