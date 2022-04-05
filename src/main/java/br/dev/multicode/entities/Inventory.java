package br.dev.multicode.entities;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "inventory")
public class Inventory {

  @Id
  @Column(name = "product_id", length = 37, nullable = false)
  private String productId;

  @Column(nullable = false)
  private Integer quantity;

  @Column(nullable = false)
  private BigDecimal price;

  @CreationTimestamp
  private ZonedDateTime createdAt;

  @UpdateTimestamp
  private ZonedDateTime updatedAt;

}
