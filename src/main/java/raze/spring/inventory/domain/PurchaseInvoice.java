package raze.spring.inventory.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class PurchaseInvoice extends BaseEntity {




    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "supplier_id", referencedColumnName = "id")
    private Supplier supplier;





    @OneToMany(mappedBy = "invoice", fetch = FetchType.EAGER,cascade = {CascadeType.ALL})
    private Set<PurchaseTransaction> transactions = new HashSet<>();

    public Set<PurchaseTransaction> getTransactions() {
        if(this.transactions == null) return new HashSet<>();
        return this.transactions;
    }



    @PrePersist
    public void beforeSave() {
        if(this.getCreatedDate() == null) {
            final LocalDateTime localDateTime = Instant.now().atZone(ZoneId.of("Asia/Tehran")).toLocalDateTime();
            this.setCreatedDate(Timestamp.valueOf(localDateTime));
        }

        if(transactions != null) transactions.forEach(t -> t.setInvoice(this));

    }

    @PreUpdate
    public void beforeUpdate() {
        final LocalDateTime localDateTime = Instant.now().atZone(ZoneId.of("Asia/Tehran")).toLocalDateTime();
        this.setModifiedDate(Timestamp.valueOf(localDateTime));

        if(transactions != null) transactions.forEach(t -> t.setInvoice(this));

    }


}
