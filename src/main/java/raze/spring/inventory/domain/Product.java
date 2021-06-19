package raze.spring.inventory.domain;


import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;


@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(indexes = @Index(columnList = "name"))
public class Product  extends BaseEntity{
    @NotBlank(message = "name is mandatory")
    @Length(min = 3, max = 30, message = "size must be between 3 and 30")
    private String name;

    @Min(value = 0, message = "must be greater than or equal 0")
    @Column( columnDefinition = "BIGINT")
    private BigInteger price;
    @Min(value = 0, message = "must be greater than or equal 0")
    @Column( columnDefinition = "BIGINT")
    private BigInteger salePrice;

    private Boolean imageAvailable = false;



    private String description;



    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Set<PurchaseTransaction> purchaseTransactions= new HashSet<>();

    public void setPurchaseTransactions(Set<PurchaseTransaction> purchaseTransactions) {
        if(purchaseTransactions == null) this.purchaseTransactions = new HashSet<>();
        else {
            this.purchaseTransactions = purchaseTransactions;
        }
    }

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private Set<SaleTransaction> saleTransactions= new HashSet<>();

    public void setSaleTransactions(Set<SaleTransaction> saleTransactions) {
        if(saleTransactions == null) this.saleTransactions = new HashSet<>();
        else this.saleTransactions = saleTransactions;
    }


    @PreRemove
    public void beforeDelete() {
        this.saleTransactions.forEach(saleTransaction -> saleTransaction.setProduct(null));
        this.purchaseTransactions.forEach(purchaseTransaction -> purchaseTransaction.setProduct(null));
    }
}
