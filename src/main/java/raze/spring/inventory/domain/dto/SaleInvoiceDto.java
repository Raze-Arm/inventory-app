package raze.spring.inventory.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleInvoiceDto {
    private UUID id;
    private CustomerDto customerDto;
    private Set<SaleTransactionDto> transactions;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private OffsetDateTime createdDate;
}
