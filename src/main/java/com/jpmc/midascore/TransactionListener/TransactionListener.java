package com.jpmc.midascore.TransactionListener;


import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class TransactionListener {
    int count = 0;
    TransactionRecordRepository transactionRepository;
    UserRepository userRepository;


    TransactionListener(TransactionRecordRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }


    @KafkaListener(topics = "${general.kafka-topic}", groupId = "midas-core")
    @Transactional
    public void listen(Transaction transaction) {
        if (!userRepository.existsById(transaction.getSenderId()) ||
                !userRepository.existsById(transaction.getRecipientId())) {
            return;
        }

        UserRecord sender = userRepository.findById(transaction.getSenderId());
        UserRecord recepient = userRepository.findById((transaction.getRecipientId()));
        float senderBalance = sender.getBalance();
        float recepientBalance = recepient.getBalance();
        float amount = transaction.getAmount();
        if (senderBalance < amount) {
            return;
        }
        sender.setBalance(senderBalance - amount);
        userRepository.save(sender);

        RestTemplate restTemplate = new RestTemplate();
        Incentive incentive = restTemplate.postForObject(
                "http://localhost:8081/incentive",
                transaction,
                Incentive.class
        );
        float incentiveAmount = incentive.getAmount();
        recepient.setBalance(recepientBalance + amount + incentiveAmount);
        userRepository.save(recepient);
        TransactionRecord record = new TransactionRecord(sender, recepient, amount, incentiveAmount);
        TransactionRecord savedRecord = transactionRepository.save(record);
        float savedIncentive = savedRecord.getIncentive();
        UserRecord wilbur = userRepository.getUserByName("wilbur");
        System.out.println("incentive :" + savedIncentive + "balance: " + wilbur.getBalance());
    }
}
