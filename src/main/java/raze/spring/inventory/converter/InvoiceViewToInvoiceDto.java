package raze.spring.inventory.converter;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import raze.spring.inventory.domain.dto.InvoiceDto;
import raze.spring.inventory.domain.view.InvoiceView;
import raze.spring.inventory.utility.DateMapper;

@Component
public class InvoiceViewToInvoiceDto implements Converter<InvoiceView, InvoiceDto> {
    private final DateMapper dateMapper;

    public InvoiceViewToInvoiceDto(DateMapper dateMapper) {
        this.dateMapper = dateMapper;
    }

    @Synchronized
    @Override
    public InvoiceDto convert(InvoiceView invoiceView) {
        return InvoiceDto.builder()
                .id(invoiceView.getId())
                .name(invoiceView.getName())
                .type(invoiceView.getType())
                .createdDate(dateMapper.asOffsetDateTime(invoiceView.getCreatedDate()))
                .build();
    }
}
