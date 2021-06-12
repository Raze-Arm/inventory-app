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
public class PurchaseInvoiceDto {
    private UUID id;
    private SupplierDto supplier;
    private Set<PurchaseTransactionDto> transactions;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ" , shape = JsonFormat.Shape.STRING)
    private OffsetDateTime createdDate;


}
