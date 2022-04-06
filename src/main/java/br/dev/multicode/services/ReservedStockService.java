package br.dev.multicode.services;

import br.dev.multicode.models.OrderMessage;

public interface ReservedStockService {

  void create(OrderMessage orderMessage);

}
