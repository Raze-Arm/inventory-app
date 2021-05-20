package raze.spring.inventory.converter;


import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import raze.spring.inventory.domain.Supplier;
import raze.spring.inventory.domain.dto.SupplierDto;

@Component
public class SupplierDtoToSupplier implements Converter<SupplierDto , Supplier> {
    @Synchronized
    @Override
    public Supplier convert(SupplierDto supplierDto) {
        return Supplier.builder()
                .firstName(supplierDto.getFirstName())
                .lastName(supplierDto.getLastName())
                .address(supplierDto.getAddress())
                .build();
    }
}
