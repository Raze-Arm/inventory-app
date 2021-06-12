package raze.spring.inventory.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PurchaseTransactionDto {
    private UUID id;
    private UUID productId;
    private String productName;
    private Long quantity;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal price;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ" , shape = JsonFormat.Shape.STRING)
    private OffsetDateTime createdDate;



}
