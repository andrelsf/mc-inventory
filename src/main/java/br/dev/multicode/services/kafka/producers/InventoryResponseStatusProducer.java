package br.dev.multicode.services.kafka.producers;

import br.dev.multicode.models.OrderProcessingStatus;
import br.dev.multicode.services.kafka.ProducerService;
import io.smallrye.mutiny.Uni;
import java.util.concurrent.CompletableFuture;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;

@ApplicationScoped
public class InventoryResponseStatusProducer implements ProducerService {

  private final Logger log = Logger.getLogger(this.getClass());

  @Inject
  @Channel("sec-response-status")
  Emitter<OrderProcessingStatus> emitter;

  @Override
  public <T> Uni<Void> sendToKafka(T message)
  {
    log.infof("Start of send message to Kafka topic sec-response-status");

    emitter.send(Message.of((OrderProcessingStatus) message)
      .withAck(() -> {
        log.infof("Message sent successfully.");
        return CompletableFuture.completedFuture(null);
      })
      .withNack(throwable -> {
        log.errorf("Message sent failed. ERROR: %s", throwable.getMessage());
        return CompletableFuture.completedFuture(null);
      }));

    return Uni.createFrom().voidItem();
  }
}
