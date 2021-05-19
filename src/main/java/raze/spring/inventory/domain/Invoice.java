package raze.spring.inventory.domain;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import raze.spring.inventory.domain.enums.InvoiceType;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID" , strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "org.hibernate.type.UUIDCharType")
    @Column(length = 36 , columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;



    @Enumerated(EnumType.STRING)
    private InvoiceType invoiceType;



    @ManyToOne
    private Supplier supplier;
    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "invoice", fetch = FetchType.EAGER)
    private Set<Transaction> transactions;



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
