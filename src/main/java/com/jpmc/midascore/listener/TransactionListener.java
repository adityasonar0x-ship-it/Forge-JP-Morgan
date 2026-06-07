package com.jpmc.midascore.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpmc.midascore.entity.Incentive;
import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import  com.jpmc.midascore.config.AppConfig.*;
import org.springframework.web.client.RestTemplate;


import java.util.Optional;


@Component
public class TransactionListener {

    private static final Logger log =
            LoggerFactory.getLogger(TransactionListener.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TransactionRecordRepository transactionRecordRepository;

    @Transactional
    @KafkaListener(topics = "${general.kafka-topic}")
    public void listen(Transaction tx) {

        Optional<UserRecord> senderOpt = userRepository.findById(tx.getSenderId());
        Optional<UserRecord> recipientOpt = userRepository.findById(tx.getRecipientId());

        if (senderOpt.isEmpty() || recipientOpt.isEmpty()) return;

        UserRecord sender = senderOpt.get();
        UserRecord recipient = recipientOpt.get();

        if (sender.getBalance() < tx.getAmount()) return;

// call incentive API
        Incentive incentive = restTemplate.postForObject(
                "http://localhost:8080/incentive",
                tx,
                Incentive.class
        );

        double bonus = (incentive != null) ? incentive.getAmount() : 0;

// update balances
        sender.setBalance(sender.getBalance() - tx.getAmount());
        recipient.setBalance(
                (float) (recipient.getBalance() + tx.getAmount() + bonus)
        );
        userRepository.findAll().forEach(u -> log.info("User: {} Balance: {}", u.getName(), u.getBalance()));
        userRepository.save(sender);
        userRepository.save(recipient);

        TransactionRecord record = new TransactionRecord();
        record.setSenderId(tx.getSenderId());
        record.setRecipientId(tx.getRecipientId());
        record.setAmount(tx.getAmount());

        transactionRecordRepository.save(record);
    }
}