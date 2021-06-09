package raze.spring.inventory.converter;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import raze.spring.inventory.domain.dto.TransactionDto;
import raze.spring.inventory.domain.view.TransactionView;
import raze.spring.inventory.utility.DateMapper;

@Component
public class TransactionViewToTransactionDto implements Converter<TransactionView, TransactionDto> {
    private final DateMapper dateMapper;

    public TransactionViewToTransactionDto(DateMapper dateMapper) {
        this.dateMapper = dateMapper;
    }

    @Synchronized
    @Override
    public TransactionDto convert(TransactionView transactionView) {
        return TransactionDto.builder()
                .id(transactionView.getId())
                .productName(transactionView.getProductName())
                .price(transactionView.getPrice())
                .quantity(transactionView.getQuantity())
                .type(transactionView.getType())
                .createdDate(dateMapper.asOffsetDateTime(transactionView.getCreatedDate()))
                .build();
    }
}
