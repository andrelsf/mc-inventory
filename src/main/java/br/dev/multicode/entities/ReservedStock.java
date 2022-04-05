package br.dev.multicode.entities;

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
@Table(name = "reserved_stock")
public class ReservedStock {

  @Id
  @Column(name = "reserved_stock_id", length = 37, nullable = false)
  private String id;

  @Column(name = "order_id", length = 37, nullable = false)
  private String orderId;

  @Column(name = "product_id", length = 37, nullable = false)
  private String productId;

  @Column(name = "reserved_quantity", nullable = false)
  private Integer reservedQuantity;

  @CreationTimestamp
  private ZonedDateTime createdAt;

  @UpdateTimestamp
  private ZonedDateTime updatedAt;

}
