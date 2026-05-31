
package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TransactionListener {

    private final UserRepository userRepository;
    private final TransactionRecordRepository transactionRepository;
    private final IncentiveService incentiveService;

    public TransactionListener(
            UserRepository userRepository,
            TransactionRecordRepository transactionRepository,
            IncentiveService incentiveService) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.incentiveService = incentiveService;
    }

    @KafkaListener(
            topics = "${general.kafka-topic}",
            groupId = "midas-core")
    @Transactional
    public void listen(Transaction transaction) {
        UserRecord sender =
                userRepository.findById(transaction.getSenderId());

        UserRecord recipient =
                userRepository.findById(transaction.getRecipientId());

        if (sender == null ||
                recipient == null ||
                sender.getBalance() < transaction.getAmount()) {
            return;
        }

        // Get incentive for this transaction
        Incentive incentive = incentiveService.getIncentive(transaction);
        float incentiveAmount = incentive.getAmount();

        // Deduct from sender (no incentive deduction)
        sender.setBalance(
                sender.getBalance() - transaction.getAmount());

        // Add transaction amount + incentive to recipient
        recipient.setBalance(
                recipient.getBalance() + transaction.getAmount() + incentiveAmount);

        userRepository.save(sender);
        userRepository.save(recipient);

        transactionRepository.save(
                new TransactionRecord(
                        sender,
                        recipient,
                        transaction.getAmount(),
                        incentiveAmount
                )
        );
    }
}
