package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionListener {

    private final DatabaseConduit databaseConduit;
    private final org.springframework.web.client.RestTemplate restTemplate;

    public TransactionListener(DatabaseConduit databaseConduit, org.springframework.boot.web.client.RestTemplateBuilder builder) {
        this.databaseConduit = databaseConduit;
        this.restTemplate = builder.build();
    }

    @KafkaListener(topics = "${general.kafka-topic}", groupId = "transaction-group")
    public void listen(Transaction transaction) {
        UserRecord sender = databaseConduit.getUser(transaction.getSenderId());
        UserRecord recipient = databaseConduit.getUser(transaction.getRecipientId());

        if (sender != null && recipient != null && sender.getBalance() >= transaction.getAmount()) {

            com.jpmc.midascore.foundation.Incentive incentiveObj = restTemplate.postForObject(
                "http://localhost:8080/incentive", transaction, com.jpmc.midascore.foundation.Incentive.class
            );

            float incentive = (incentiveObj != null) ? incentiveObj.getAmount() : 0f;

            sender.setBalance(sender.getBalance() - transaction.getAmount());
            recipient.setBalance(recipient.getBalance() + transaction.getAmount() + incentive);

            databaseConduit.save(sender);
            databaseConduit.save(recipient);

            TransactionRecord record = new TransactionRecord(sender, recipient, transaction.getAmount(), incentive);
            databaseConduit.save(record);
        }
    }
}
