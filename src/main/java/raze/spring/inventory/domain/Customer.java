package raze.spring.inventory.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(indexes = @Index(columnList = "firstName, lastName"))
public class Customer extends BaseEntity {

    @NotBlank(message = "last name is mandatory")
    @Length(min = 3, max = 30, message = "size must be between 3 and 30")
    private String firstName;
    @NotBlank(message = "last name is mandatory")
    @Length(min = 3, max = 30, message = "size must be between 3 and 30")
    private String lastName;

    private String address;

    @OneToMany(mappedBy = "customer" , cascade = CascadeType.ALL)
    private Set<SaleInvoice> invoices = new HashSet<>();

    public Set<SaleInvoice> getInvoices() {
        if(this.invoices == null) return new HashSet<>();
        return  this.invoices;
    }

}
