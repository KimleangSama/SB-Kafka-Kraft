package com.keakimleang.demokafka;

import com.keakimleang.demokafka.event.DemoProducedEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    @KafkaListener(topics = "demo-topic", groupId = "demo-group")
    public void listen(ConsumerRecord<String, DemoProducedEvent> record) {
        System.out.println("Consumed object: " + record.value());
        System.out.println("Consumed message: " + record.value().getMessage());
    }
}