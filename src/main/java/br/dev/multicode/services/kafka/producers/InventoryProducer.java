package br.dev.multicode.services.kafka.producers;

import br.dev.multicode.models.OrderInventoryStatus;
import java.util.concurrent.CompletableFuture;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;

@ApplicationScoped
public class InventoryProducer {

  private final Logger log = Logger.getLogger(this.getClass());

  @Inject
  @Channel("sec-replay")
  Emitter<OrderInventoryStatus> emitter;

  public void sendToKafka(OrderInventoryStatus orderInventoryStatus)
  {
    emitter.send(Message.of(orderInventoryStatus)
        .withAck(() -> {
          log.infof("Message sent successfully. eventId=%s", orderInventoryStatus.getEventId());
          return CompletableFuture.completedFuture(null);
        })
        .withNack(throwable -> {
          log.errorf("Message sent failed. ERROR: %s", throwable.getMessage());
          return CompletableFuture.completedFuture(null);
        }));
  }

}
