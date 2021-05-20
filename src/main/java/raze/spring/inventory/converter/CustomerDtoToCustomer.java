package raze.spring.inventory.converter;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import raze.spring.inventory.domain.Customer;
import raze.spring.inventory.domain.dto.CustomerDto;

@Component
public class CustomerDtoToCustomer implements Converter<CustomerDto, Customer> {
    @Synchronized
    @Override
    public Customer convert(CustomerDto customerDto) {
        return Customer.builder()
                .firstName(customerDto.getFirstName())
                .lastName(customerDto.getLastName())
                .address(customerDto.getAddress())
                .build();
    }
}
