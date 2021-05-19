package raze.spring.inventory.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleTransactionDto {
    private UUID id ;

    private String description;
    private Long quantity;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal price;

    private UUID productId;
    private String productName;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ" , shape = JsonFormat.Shape.STRING)
    private OffsetDateTime createdDate;
}
