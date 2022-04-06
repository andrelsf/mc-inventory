package br.dev.multicode.repositories;

import br.dev.multicode.entities.Inventory;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import javax.ws.rs.NotFoundException;

@ApplicationScoped
public class InventoryRepository implements PanacheRepository<Inventory> {

  @Transactional(TxType.NEVER)
  public Inventory findByProductId(final String productId, Integer requestedQuantity)
  {
    return this.find("product_id = :productId AND quantity >= :requestedQuantity",
            Parameters.with("productId", productId).and("requestedQuantity", requestedQuantity))
        .firstResultOptional()
        .orElseThrow(() -> new NotFoundException("Product not found by ID=".concat(productId)));
  }

  @Transactional
  public void save(Inventory inventory) {
    this.persistAndFlush(inventory);
  }
}
