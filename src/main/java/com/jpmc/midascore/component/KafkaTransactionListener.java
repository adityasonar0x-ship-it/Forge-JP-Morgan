package com.jpmc.midascore.component;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaTransactionListener {

    @KafkaListener(topics = "${general.kafka-topic}", groupId = "midas-core")
    public void listen(String transactionLine) {
        System.out.println("Received transaction: " + transactionLine);
    }
}