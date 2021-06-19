package raze.spring.inventory.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;


import javax.persistence.*;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.math.BigInteger;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class SaleTransaction  extends BaseEntity{

    private String description;

    @Min(value = 1 , message = "must be greater than or equal 1")
    private Long quantity;

    @Min(value = 0 , message = "must be greater than or equal 0")
    @Column( columnDefinition = "BIGINT")
    private BigInteger price;



    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;

    @Length(min = 3, max = 30, message = "size must be between 3 and 30")
    private String productName;

    @ManyToOne
    private SaleInvoice invoice;

    @PrePersist
    public void beforeTrSave() {
        if(this.product != null) {
            this.productName = product.getName();
        }
    }

}
