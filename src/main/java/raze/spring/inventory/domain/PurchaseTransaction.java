package raze.spring.inventory.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;


import javax.persistence.*;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

import java.util.UUID;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PurchaseTransaction extends BaseEntity {

    private String description;

    @Min(value = 1 , message = "must be greater than or equal 1")
    private Long quantity;

    @Min(value = 0 , message = "must be greater than or equal 0")
    private BigDecimal price;



    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;

    @Length(min = 3, max = 30, message = "size must be between 3 and 30")
    private String productName;




    public UUID getProductId() {return  this.product != null ? product.getId() : null ;}

    @ManyToOne
    private PurchaseInvoice invoice;

    @PrePersist
    public void beforeTrSave() {
        if(this.product != null) {
            this.productName = product.getName();
        }
    }

}
