package com.jpmc.midascore;

import com.jpmc.midascore.foundation.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionListener {

    @Autowired
    private TransactionService transactionService;

    @KafkaListener(topics = "${general.kafka-topic}", groupId = "midas")
    public void listen(Transaction transaction) {
        System.out.println("Received: " + transaction);
        transactionService.processTransaction(transaction);
    }
}