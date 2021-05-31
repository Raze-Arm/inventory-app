package raze.spring.inventory.domain.view;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "transaction_view")
public class TransactionView {
    @Id
    @Type(type="uuid-char")
    private UUID id;
    private String productName;
    private BigDecimal price;
    private Long quantity;
    private String type;
    private Timestamp createdDate;

}
