package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DatabaseConduit {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private IncentiveService incentiveService;

    public void save(UserRecord userRecord) {
        userRepository.save(userRecord);
    }

    public void process(Transaction transaction) {
        Optional<UserRecord> senderOpt = userRepository.findById(transaction.getSenderId());
        if (senderOpt.isEmpty()) return;

        Optional<UserRecord> recipientOpt = userRepository.findById(transaction.getRecipientId());
        if (recipientOpt.isEmpty()) return;

        UserRecord sender = senderOpt.get();
        UserRecord recipient = recipientOpt.get();

        if (sender.getBalance() < transaction.getAmount()) return;

        float incentive = incentiveService.getIncentive(transaction);

        sender.setBalance(sender.getBalance() - transaction.getAmount());
        recipient.setBalance(recipient.getBalance() + transaction.getAmount() + incentive);

        userRepository.save(sender);
        userRepository.save(recipient);

        TransactionRecord record = new TransactionRecord(sender, recipient, transaction.getAmount(), incentive);
        transactionRepository.save(record);
    }

    public void printAllUsers() {
        userRepository.findAll().forEach(u ->
            System.out.println("USER: " + u.getName() + " | BALANCE: " + u.getBalance())
        );
    }
}