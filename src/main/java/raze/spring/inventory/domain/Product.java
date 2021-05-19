package raze.spring.inventory.domain;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID" , strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "org.hibernate.type.UUIDCharType")
    @Column(length = 36 , columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;

    private String name;

    private BigDecimal price;
    private BigDecimal salePrice;



<<<<<<< HEAD
    private String description;


=======
>>>>>>> adding SaleInvoiceDto and SaleTransactionDto  closes #15 and fixing project errors

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private Set<PurchaseTransaction> purchaseTransactions= new HashSet<>();



    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private Set<SaleTransaction> saleTransactions= new HashSet<>();




    private Timestamp createdDate;

    private Timestamp modifiedDate;

    @PrePersist
    public void beforeSave() {
        if(createdDate == null) {
            createdDate =  Timestamp.from(Instant.now());
        }
    }

    @PreUpdate
    public void beforeUpdate() {
        modifiedDate =  Timestamp.from(Instant.now());
    }
}
