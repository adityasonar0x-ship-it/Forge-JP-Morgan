package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionIncentiveProcessor {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public void process(Transaction transaction) {

        // 1️⃣ Fetch users directly (NOT Optional)
        UserRecord sender =
                userRepository.findById(transaction.getSenderId());

        UserRecord recipient =
                userRepository.findById(transaction.getRecipientId());

        // 2️⃣ Validate sender & recipient
        if (sender == null || recipient == null) {
            return;
        }

        // 3️⃣ Check sender balance
        if (sender.getBalance() < transaction.getAmount()) {
            return;
        }

        // 4️⃣ Update balances
        sender.setBalance(sender.getBalance() - transaction.getAmount());
        recipient.setBalance(recipient.getBalance() + transaction.getAmount());

        userRepository.save(sender);
        userRepository.save(recipient);

        // 5️⃣ Save transaction
        TransactionRecord record =
                new TransactionRecord(sender, recipient, transaction.getAmount());

        transactionRepository.save(record);
    }
}
