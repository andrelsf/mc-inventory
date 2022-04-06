package br.dev.multicode.repositories;

import br.dev.multicode.entities.ReservedStock;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
public class ReservedStockRepository implements PanacheRepository<ReservedStock> {

  @Transactional
  public void save(final ReservedStock reservedStock)
  {
    this.persistAndFlush(reservedStock);
  }

  @Transactional
  public void save(final Set<ReservedStock> reservedStocks)
  {
    this.persist(reservedStocks);
  }
}
