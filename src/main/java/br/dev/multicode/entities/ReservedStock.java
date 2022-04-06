package br.dev.multicode.entities;

import br.dev.multicode.models.ItemMessage;
import br.dev.multicode.models.OrderMessage;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
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
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private String id;

  @Column(name = "order_id", length = 37, nullable = false)
  private String orderId;

  @Column(name = "product_id", length = 37, nullable = false)
  private String productId;

  @Column(name = "reserved_quantity", nullable = false)
  private Integer reservedQuantity;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  private ZonedDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private ZonedDateTime updatedAt;

  public static Set<ReservedStock> of(OrderMessage orderMessage) {
    return orderMessage.getItems()
        .stream()
        .map(itemMessage -> ReservedStock.of(orderMessage.getOrderId(), itemMessage))
        .collect(Collectors.toSet());
  }

  public static ReservedStock of(UUID orderId, ItemMessage itemMessage)
  {
    return ReservedStock.builder()
        .orderId(orderId.toString())
        .productId(itemMessage.getProductId())
        .reservedQuantity(itemMessage.getAmount())
        .build();
  }
}
