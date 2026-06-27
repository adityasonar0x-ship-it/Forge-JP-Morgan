package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConduit {
    private final UserRepository userRepository;
    private final TransactionRecordRepository transactionRecordRepository;

    public DatabaseConduit(UserRepository userRepository, TransactionRecordRepository transactionRecordRepository) {
        this.userRepository = userRepository;
        this.transactionRecordRepository = transactionRecordRepository;
    }

    public void saveUser(UserRecord userRecord) {
        userRepository.save(userRecord);
    }

    public UserRecord findUserById(long userId) {
        return userRepository.findById(userId);
    }

    public void saveTransaction(TransactionRecord transactionRecord) {
        transactionRecordRepository.save(transactionRecord);
    }

}
