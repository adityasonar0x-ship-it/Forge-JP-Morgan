package com.jpmc.midascore.component;
import org.springframework.stereotype.Component;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.kafka.annotation.KafkaListener;

@Component
public class TransactionListener {
    @KafkaListener(topics = "${general.kafka-topic}", groupId = "midascore-group")
    public void listen(Transaction transaction) {
    }
}
