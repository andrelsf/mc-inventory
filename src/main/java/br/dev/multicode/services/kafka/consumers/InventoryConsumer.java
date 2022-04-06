package br.dev.multicode.services.kafka.consumers;

import br.dev.multicode.models.OrderMessage;
import br.dev.multicode.services.InventoryService;
import io.smallrye.reactive.messaging.kafka.api.IncomingKafkaRecordMetadata;
import java.util.concurrent.CompletionStage;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;

@ApplicationScoped
public class InventoryConsumer {

  private final Logger logger = Logger.getLogger(this.getClass());

  @Inject
  InventoryService inventoryService;

  @Incoming("sec-inventory")
  public CompletionStage<Void> receiveFromKafkaTopicSecInventory(Message<OrderMessage> orderMessage)
  {
    var metadata = orderMessage.getMetadata(IncomingKafkaRecordMetadata.class)
        .orElseThrow();

    final OrderMessage orderMessageReceived = orderMessage.getPayload();
    logger.infof("INVENTORY: %s - Got a order message: %s", metadata.getTopic(),
        orderMessageReceived.getOrderId());

    inventoryService.processOrder(orderMessageReceived);
    return orderMessage.ack();
  }

}
