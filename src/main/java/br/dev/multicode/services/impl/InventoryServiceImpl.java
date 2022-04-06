package br.dev.multicode.services.impl;

import br.dev.multicode.entities.Inventory;
import br.dev.multicode.entities.ReservedStock;
import br.dev.multicode.enums.OrderStatus;
import br.dev.multicode.models.ItemMessage;
import br.dev.multicode.models.OrderMessage;
import br.dev.multicode.models.OrderProcessingStatus;
import br.dev.multicode.repositories.InventoryRepository;
import br.dev.multicode.services.InventoryService;
import br.dev.multicode.services.ReservedStockService;
import br.dev.multicode.services.kafka.producers.InventoryResponseStatusProducer;
import java.util.HashSet;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;

@ApplicationScoped
public class InventoryServiceImpl implements InventoryService {

  @Inject
  InventoryResponseStatusProducer inventoryProducer;
  @Inject InventoryRepository inventoryRepository;
  @Inject ReservedStockService reservedStockService;

  @Override
  public void processOrder(OrderMessage orderMessage) {
    final Set<String> itemsOutOfStock = getItemsOutOfStock(orderMessage.getItems());

    if (!itemsOutOfStock.isEmpty()) {
      inventoryProducer.doNotification(OrderProcessingStatus.of(orderMessage, OrderStatus.REJECTED_PRODUCT));
      throw new NotFoundException("One or more items out of stock");
    }

    makeReserve(orderMessage);

    inventoryProducer.doNotification(OrderProcessingStatus.of(orderMessage, OrderStatus.RESERVED_PRODUCTS));
  }

  private void makeReserve(OrderMessage orderMessage) {
    orderMessage.getItems()
        .forEach(itemMessage -> {
          Inventory inventoryFound = inventoryRepository.findByProductId(itemMessage.getProductId(), itemMessage.getAmount());
          inventoryFound.withdraw(itemMessage.getAmount());
          reservedStockService.create(ReservedStock.of(orderMessage.getOrderId(), itemMessage));
        });
  }

  private Set<String> getItemsOutOfStock(Set<ItemMessage> items) {
    Set<String> itemsOutOfStock = new HashSet<>();
    items.forEach(itemMessage -> {
        try {
          inventoryRepository.findByProductId(itemMessage.getProductId(), itemMessage.getAmount());
        } catch (NotFoundException ex) {
          itemsOutOfStock.add(itemMessage.getProductId());
        }
      });
    return itemsOutOfStock;
  }
}
