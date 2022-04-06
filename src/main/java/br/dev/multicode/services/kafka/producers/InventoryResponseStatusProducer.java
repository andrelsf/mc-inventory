package br.dev.multicode.services.kafka.producers;

import br.dev.multicode.models.OrderProcessingStatus;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import java.util.concurrent.CompletableFuture;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;

@ApplicationScoped
public class InventoryResponseStatusProducer {

  private final Logger log = Logger.getLogger(this.getClass());

  @Inject
  @Channel("sec-response-status")
  Emitter<OrderProcessingStatus> emitter;

  public void doNotification(OrderProcessingStatus orderInventoryStatus)
  {
    Uni.createFrom()
      .item(orderInventoryStatus)
      .emitOn(Infrastructure.getDefaultWorkerPool())
      .subscribe()
      .with(this::sendToKafka, Throwable::new);
  }

  private Uni<Void> sendToKafka(final OrderProcessingStatus orderInventoryStatus)
  {
    log.infof("Start of send message to Kafka topic sec-response-status");

    emitter.send(Message.of(orderInventoryStatus)
      .withAck(() -> {
        log.infof("Message sent successfully. eventId=%s", orderInventoryStatus.getEventId());
        return CompletableFuture.completedFuture(null);
      })
      .withNack(throwable -> {
        log.errorf("Message sent failed. ERROR: %s", throwable.getMessage());
        return CompletableFuture.completedFuture(null);
      }));

    return Uni.createFrom().voidItem();
  }
}
