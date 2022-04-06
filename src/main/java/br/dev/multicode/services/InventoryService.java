package br.dev.multicode.services;

import br.dev.multicode.models.OrderMessage;

public interface InventoryService {

  void processOrder(OrderMessage orderMessage);

}
