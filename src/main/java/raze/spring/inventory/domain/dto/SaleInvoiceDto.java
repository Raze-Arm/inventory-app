package raze.spring.inventory.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SaleInvoiceDto {
    private UUID id;

    private CustomerDto customer;

    private Set<SaleTransactionDto> transactions;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private OffsetDateTime createdDate;
}
