package com.jpmc.midascore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;

@Service
public class TransactionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RestTemplate restTemplate;

    public void processTransaction(Transaction transaction) {

        // Step 1: Check sender exists
        UserRecord sender = userRepository.findById(transaction.getSenderId());
        if (sender == null) {
            System.out.println("Invalid: sender not found");
            return;
        }

        // Step 2: Check recipient exists
        UserRecord recipient = userRepository.findById(transaction.getRecipientId());
        if (recipient == null) {
            System.out.println("Invalid: recipient not found");
            return;
        }

        // Step 3: Check sender has enough balance
        if (sender.getBalance() < transaction.getAmount()) {
            System.out.println("Invalid: insufficient balance");
            return;
        }

        // Step 4: Call the incentive API
        float incentiveAmount = 0;
        try {
            Incentive incentive = restTemplate.postForObject(
                "http://localhost:8080/incentive",
                transaction,
                Incentive.class
            );
            if (incentive != null) {
                incentiveAmount = incentive.getAmount();
            }
        } catch (Exception e) {
            System.out.println("Incentive API error: " + e.getMessage());
        }

        // Step 5: Update balances
        // Sender loses the transaction amount
        sender.setBalance(sender.getBalance() - transaction.getAmount());
        // Recipient gains transaction amount PLUS incentive
        recipient.setBalance(recipient.getBalance() + transaction.getAmount() + incentiveAmount);

        // Step 6: Save users
        userRepository.save(sender);
        userRepository.save(recipient);

        // Step 7: Save transaction with incentive
        TransactionRecord record = new TransactionRecord(sender, recipient, transaction.getAmount(), incentiveAmount);
        transactionRepository.save(record);

        System.out.println("Saved! Amount: " + transaction.getAmount() + " Incentive: " + incentiveAmount);

        // Step 8: Print wilbur's balance
        userRepository.findAll().forEach(user -> {
            if (user.getName() != null && user.getName().toLowerCase().contains("wilbur")) {
                System.out.println(">>> WILBUR BALANCE: " + user.getBalance());
            }
        });
    }
}