package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TransactionListener {
    private static final Logger logger = LoggerFactory.getLogger(TransactionListener.class);
    private final DatabaseConduit databaseConduit;
    private final IncentiveApiClient incentiveApiClient;

    public TransactionListener(DatabaseConduit databaseConduit, IncentiveApiClient incentiveApiClient) {
        this.databaseConduit = databaseConduit;
        this.incentiveApiClient = incentiveApiClient;
    }

    @KafkaListener(topics = "${general.kafka-topic}", groupId = "midas-core-group")
    @Transactional
    public void receive(Transaction transaction) {
        UserRecord sender = databaseConduit.findUserById(transaction.getSenderId());
        UserRecord recipient = databaseConduit.findUserById(transaction.getRecipientId());

        if (sender == null || recipient == null) {
            logger.info("Discarding invalid transaction (unknown user): {}", transaction);
            return;
        }

        if (sender.getBalance() < transaction.getAmount()) {
            logger.info("Discarding invalid transaction (insufficient balance): {}", transaction);
            return;
        }

        float incentiveAmount = incentiveApiClient.fetchIncentiveAmount(transaction);

        sender.setBalance(sender.getBalance() - transaction.getAmount());
        recipient.setBalance(recipient.getBalance() + transaction.getAmount() + incentiveAmount);

        databaseConduit.saveUser(sender);
        databaseConduit.saveUser(recipient);
        databaseConduit.saveTransaction(new TransactionRecord(sender, recipient, transaction.getAmount(), incentiveAmount));

        logger.info("Processed transaction: {}", transaction);
    }
}

