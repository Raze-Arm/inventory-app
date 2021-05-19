package raze.spring.inventory.domain;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID" , strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "org.hibernate.type.UUIDCharType")
    @Column(length = 36 , columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;

    private String description;

    private Long quantity;

    private BigDecimal price;



    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Product product;


    @ManyToOne
    private Invoice invoice;





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
