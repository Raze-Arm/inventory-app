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
public class PurchaseInvoice {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID" , strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "org.hibernate.type.UUIDCharType")
    @Column(length = 36 , columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;




    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "supplier_id", referencedColumnName = "id")
    private Supplier supplier;





    @OneToMany(mappedBy = "invoice", fetch = FetchType.EAGER,cascade = {CascadeType.ALL})
    private Set<PurchaseTransaction> transactions = new HashSet<>();

    public Set<PurchaseTransaction> getTransactions() {
        if(this.transactions == null) return new HashSet<>();
        return this.transactions;
    }



    private Timestamp createdDate;

    private Timestamp modifiedDate;

    @PrePersist
    public void beforeSave() {
        if(createdDate == null) {
            createdDate =  Timestamp.from(Instant.now());
        }

        if(transactions != null) transactions.forEach(t -> t.setInvoice(this));

    }

    @PreUpdate
    public void beforeUpdate() {
        modifiedDate =  Timestamp.from(Instant.now());

        if(transactions != null) transactions.forEach(t -> t.setInvoice(this));

    }


}
