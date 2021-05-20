package raze.spring.inventory.domain.view;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "product_view")
public class ProductView {
      @Id private UUID id;
      private String name;
      private String description;
      private BigDecimal price;
      private BigDecimal salePrice;
      private Long quantity;
}
