package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.jpmc.midascore.entity.TransactionRecord;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final IncentiveService incentiveService;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository, IncentiveService incentiveService) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.incentiveService = incentiveService;
    }

    @Transactional
    public boolean processTransaction(Transaction transaction) {
        UserRecord sender = userRepository.findById(transaction.getSenderId());
        UserRecord recipient = userRepository.findById(transaction.getRecipientId());

        if (!isValidTransaction(sender, recipient, transaction.getAmount())) {
            System.out.println("Invalid Transaction" + transaction);
            return false;
        }

        float incentive = incentiveService.getIncentive(transaction);

        sender.setBalance(sender.getBalance() - transaction.getAmount());
        recipient.setBalance(recipient.getBalance() + transaction.getAmount() +  incentive);

        userRepository.save(sender);
        userRepository.save(recipient);

        TransactionRecord record = new TransactionRecord(transaction.getAmount(), sender, recipient, incentive);
        transactionRepository.save(record);

        System.out.println("Transaction Successful" + record);
        return true;
    }

    public boolean isValidTransaction(UserRecord sender, UserRecord recipient, float amount) {
        if (sender == null) {
            return false;
        }
        if (recipient == null) {
            return false;
        }
        return !(sender.getBalance() < amount);
    }
}