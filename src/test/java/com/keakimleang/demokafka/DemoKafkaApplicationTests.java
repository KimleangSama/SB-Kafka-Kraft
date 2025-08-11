package com.keakimleang.demokafka;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

@SpringBootTest
class DemoKafkaApplicationTests {
    @Autowired
    private KafkaProducerService producerService;

    @Test
    void contextLoads() {
        loadTestKafkaProducers(producerService);
    }

    public void loadTestKafkaProducers(KafkaProducerService producerService) {
        int producersCount = 10;
        AtomicInteger sentCounter = new AtomicInteger();
        var startTime = System.currentTimeMillis();
        Flux.range(1, producersCount)
                .flatMap(i -> producerService.send("demo-topic", "message-" + i)
                                .doOnSuccess(v -> {
                                    int sent = sentCounter.incrementAndGet();
                                    if (sent % 10000 == 0) System.out.println("Sent " + sent + " messages");
                                })
                                .doOnError(e -> System.err.println("Error sending message " + i + ": " + e.getMessage()))
                        , 1000) // concurrency of 1000 to avoid overwhelming
                .blockLast(); // wait for all to complete
        var endTime = System.currentTimeMillis();
        System.out.println("Sent " + sentCounter.get() + " messages in " + (endTime - startTime) + " ms");
    }

}
