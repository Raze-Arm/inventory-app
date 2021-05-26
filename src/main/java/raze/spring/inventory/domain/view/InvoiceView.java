package raze.spring.inventory.domain.view;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "invoice_view")
public class InvoiceView {
    @Id
    @Type(type="uuid-char")
    private  UUID id;
    private String name;
    private String type;
    private Timestamp createdDate;
}
