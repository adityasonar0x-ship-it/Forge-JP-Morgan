package com.jpmc.midascore.TransactionListener;


import com.jpmc.midascore.foundation.Transaction;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionListener {
    int count=0;
    @KafkaListener(topics = "${general.kafka-topic}", groupId = "midas-core")
    public void listen(Transaction transaction){
        count++;
        System.out.println(count+" "+transaction.getAmount());
    }
}
