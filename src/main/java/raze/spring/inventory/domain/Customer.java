package raze.spring.inventory.domain;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
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
@Table(indexes = @Index(columnList = "firstName, lastName"))
public class Customer {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID" , strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "org.hibernate.type.UUIDCharType")
    @Column(length = 36 , columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;
    @NotBlank(message = "last name is mandatory")
    @Length(min = 3, max = 20, message = "size must be between 2 and 30")
    private String firstName;
    @NotBlank(message = "last name is mandatory")
    @Length(min = 3, max = 20, message = "size must be between 2 and 30")
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
