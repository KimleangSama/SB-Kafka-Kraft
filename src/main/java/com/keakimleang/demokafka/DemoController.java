package com.keakimleang.demokafka;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class DemoController {
    private final String site;
    private final KafkaProducerService producerService;

    public DemoController(KafkaProducerService producerService) {
        this.producerService = producerService;
        site = System.getenv().getOrDefault("SITE", "NONE");
    }

    @GetMapping("/info")
    public String info() {
        return "Hello from " + site + " backend!";
    }

    @PostMapping("/send")
    public Mono<String> sendMessage(@RequestParam String message) {
        return producerService.send("demo-topic", message)
                .thenReturn("Sent: " + message);
    }
}