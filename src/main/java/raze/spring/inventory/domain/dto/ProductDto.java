package raze.spring.inventory.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
<<<<<<< HEAD
import java.util.UUID;
=======
>>>>>>> adding SaleInvoiceDto and SaleTransactionDto  closes #15 and fixing project errors

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
<<<<<<< HEAD
    private UUID id;
=======
    private String id;
>>>>>>> adding SaleInvoiceDto and SaleTransactionDto  closes #15 and fixing project errors
    private String name;
    private Long quantity;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal price;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal salePrice;
<<<<<<< HEAD
    private String description;
=======
>>>>>>> adding SaleInvoiceDto and SaleTransactionDto  closes #15 and fixing project errors
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ" , shape = JsonFormat.Shape.STRING)
    private OffsetDateTime createdDate;
}
