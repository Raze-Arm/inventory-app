package raze.spring.inventory.converter;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import raze.spring.inventory.domain.Supplier;
import raze.spring.inventory.domain.dto.SupplierDto;
import raze.spring.inventory.utility.DateMapper;

@Component
public class SupplierToSupplierDto implements Converter<Supplier, SupplierDto> {
    private final DateMapper dateMapper;

    public SupplierToSupplierDto(DateMapper dateMapper) {
        this.dateMapper = dateMapper;
    }

    @Synchronized
    @Override
    public SupplierDto convert(Supplier supplier) {
        return SupplierDto.builder()
                .id(supplier.getId())
                .firstName(supplier.getFirstName())
                .lastName(supplier.getLastName())
                .address(supplier.getAddress())
                .createdDate(dateMapper.asOffsetDateTime(supplier.getCreatedDate()))
                .build();
    }
}
