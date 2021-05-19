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
<<<<<<< HEAD
    private CustomerDto customer;
=======
    private CustomerDto customerDto;
>>>>>>> adding SaleInvoiceDto and SaleTransactionDto  closes #15 and fixing project errors
    private Set<SaleTransactionDto> transactions;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private OffsetDateTime createdDate;
}
