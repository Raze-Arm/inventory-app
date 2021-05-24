package raze.spring.inventory.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import raze.spring.inventory.converter.CustomerDtoToCustomer;
import raze.spring.inventory.converter.CustomerToCustomerDto;
import raze.spring.inventory.service.CustomerService;
import raze.spring.inventory.domain.Customer;
import raze.spring.inventory.domain.dto.CustomerDto;
import raze.spring.inventory.repository.CustomerRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerDtoToCustomer customerDtoToCustomer;
    private final CustomerToCustomerDto customerToCustomerDto;

      public CustomerServiceImpl(
          CustomerRepository customerRepository,
          CustomerDtoToCustomer customerDtoToCustomer,
          CustomerToCustomerDto customerToCustomerDto) {
        this.customerRepository = customerRepository;
        this.customerDtoToCustomer = customerDtoToCustomer;
        this.customerToCustomerDto = customerToCustomerDto;
      }

    @Override
    public Page<CustomerDto> getCustomerPage(int page, int size, String sort, String search) {
          final Pageable pageable = PageRequest.of(page, size, Sort.by(sort != null ? sort : "id"));
          if(search.length() > 0) {
              return this.customerRepository.findAll(pageable, search).map(this.customerToCustomerDto::convert);
          } else {
              return this.customerRepository.findAll(pageable).map(this.customerToCustomerDto::convert);
          }

    }

    @Transactional
    @Override
    public List<CustomerDto> getCustomerList() {
        return this.customerRepository.findAll().stream()
            .map(this.customerToCustomerDto::convert)
            .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CustomerDto getCustomer(UUID id) {
        return this.customerToCustomerDto.convert(this.customerRepository.findById(id).orElseThrow());
    }

    @Transactional
    @Override
    public UUID saveCustomer(CustomerDto customerDto) {
        final Customer customer =
            this.customerRepository.save(
                Objects.requireNonNull(this.customerDtoToCustomer.convert(customerDto)));

        return customer.getId();
    }

    @Transactional
    @Override
    public void updateCustomer(CustomerDto customerDto) {
        if(customerDto.getId() == null) throw new NoSuchElementException();
        final Customer customerToEdit = this.customerRepository.findById(customerDto.getId()).orElseThrow();
        customerToEdit.setFirstName(customerDto.getFirstName());
        customerToEdit.setLastName(customerDto.getLastName());
        customerToEdit.setAddress(customerDto.getAddress());
    }

    @Override
    public void deleteCustomer(UUID id) {
        this.customerRepository.deleteById(id);
    }
}
