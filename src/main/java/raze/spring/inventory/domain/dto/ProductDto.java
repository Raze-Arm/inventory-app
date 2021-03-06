package raze.spring.inventory.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.OffsetDateTime;

import java.util.UUID;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {

    private UUID id;

    private String name;
    private Long quantity;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigInteger price;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigInteger salePrice;

    private MultipartFile image;
    private Boolean imageAvailable;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ" , shape = JsonFormat.Shape.STRING)
    private OffsetDateTime createdDate;
}
