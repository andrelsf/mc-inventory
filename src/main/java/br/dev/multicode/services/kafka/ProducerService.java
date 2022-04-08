package br.dev.multicode.services.kafka;

import io.smallrye.mutiny.Uni;

public interface ProducerService {

  <T> Uni<Void> sendToKafka(T message);

}
