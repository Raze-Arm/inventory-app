package raze.spring.inventory.domain;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Customer {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID" , strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "org.hibernate.type.UUIDCharType")
    @Column(length = 36 , columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;

    private String firstName;
    private String lastName;

    private String address;

    @OneToMany(mappedBy = "customer" , cascade = CascadeType.ALL)
    private Set<SaleInvoice> invoices = new HashSet<>();

    public Set<SaleInvoice> getInvoices() {
        if(this.invoices == null) return new HashSet<>();
        return  this.invoices;
    }

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
