package com.jpmc.midascore.component;

import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.service.TransactionService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaTransactionListener {
    private final TransactionService transactionService;

    public KafkaTransactionListener(TransactionService transactionService) {
        this.transactionService = transactionService;
    }


    @KafkaListener(topics = "${general.kafka-topic}", groupId = "midas-core")
    public void listen(Transaction transaction){
//        System.out.println("Transaction Amount : "+transaction.getAmount());
        transactionService.processTransaction(transaction);
    }

}
