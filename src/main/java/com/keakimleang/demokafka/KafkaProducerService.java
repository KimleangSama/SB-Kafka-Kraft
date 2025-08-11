package com.keakimleang.demokafka;

import com.keakimleang.demokafka.event.DemoProducedEvent;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class KafkaProducerService {
    private final KafkaTemplate<String, DemoProducedEvent> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, DemoProducedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public Mono<Void> send(String topic, String message) {
        DemoProducedEvent event = new DemoProducedEvent();
        event.setId(UUID.randomUUID().toString());
        event.setMessage(message);
        CompletableFuture<SendResult<String, DemoProducedEvent>> future = kafkaTemplate.send(topic, event.getId(), event);
        return Mono.fromFuture(future)
                .doOnSuccess(result -> System.out.println("Sent message: " + message))
                .doOnError(error -> System.err.println("Failed to send message: " + error.getMessage()))
                .then();
    }
}