package br.dev.multicode.services.impl;

import br.dev.multicode.entities.ReservedStock;
import br.dev.multicode.repositories.ReservedStockRepository;
import br.dev.multicode.services.ReservedStockService;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ReservedStockServiceImpl implements ReservedStockService {

  @Inject
  ReservedStockRepository repository;

  @Override
  public void create(ReservedStock reservedStock)
  {
    repository.save(reservedStock);
  }
}
