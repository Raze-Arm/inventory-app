package raze.spring.inventory.domain.view;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.UUID;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "product_view")
public class ProductView {
      @Id
      @Type(type="uuid-char")
      private UUID id;
      private String name;
      private String description;
      @Column( columnDefinition = "BIGINT")
      private BigInteger price;
      @Column( columnDefinition = "BIGINT")
      private BigInteger salePrice;
      private Long quantity;
      private Boolean imageAvailable;
      private Timestamp createdDate;
}
