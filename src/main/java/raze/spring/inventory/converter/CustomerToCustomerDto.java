package raze.spring.inventory.converter;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import raze.spring.inventory.domain.Customer;
import raze.spring.inventory.domain.dto.CustomerDto;
import raze.spring.inventory.utility.DateMapper;

@Component
public class CustomerToCustomerDto implements Converter<Customer, CustomerDto> {
    private final DateMapper dateMapper;

    public CustomerToCustomerDto(DateMapper dateMapper) {
        this.dateMapper = dateMapper;
    }

    @Synchronized
    @Override
    public CustomerDto convert(Customer customer) {
        return CustomerDto.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .address(customer.getAddress())
                .createdDate(dateMapper.asOffsetDateTime(customer.getCreatedDate()))
                .build();
    }
}
