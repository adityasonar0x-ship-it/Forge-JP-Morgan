package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransactionService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    private IncentiveService incentiveService;

    // Adding repositories in constructor
    public TransactionService(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    //Method for validating anf processing Transaction
    public void processTransaction(Transaction transaction){

//        Optional<UserRecord> sender = userRepository.findById(transaction.getSenderId());
//        Optional<UserRecord> recipient = userRepository.findById(transaction.getRecipientId());
//        float transactionAmount = transaction.getAmount();
//        if(sender==null|| recipient == null){
//            return;
//        }else if(transactionAmount>sender.get().getBalance()){
//            return;
//        }else{
//            //Updating Sender balance
//            sender.get().setBalance(sender.get().getBalance()-transactionAmount);
//
//            //Updating Recipient balance
//            recipient.get().setBalance(recipient.get().getBalance()+transactionAmount);
//        }
//        //Saving Data
//        userRepository.save(sender);
//        userRepository.save(recipient);
//
//        TransactionRecord record =
//                new TransactionRecord(sender., recipient.getId(), amount);
//
//        transactionRepository.save(record);


        float transactionAmount = transaction.getAmount();

        //finding userOpt
        Optional<UserRecord> senderOpt = userRepository.findById(transaction.getSenderId());
        Optional<UserRecord> recipientOpt = userRepository.findById(transaction.getRecipientId());

        // Validate users exist or not
        if (senderOpt.isEmpty() || recipientOpt.isEmpty()) {
            return;
        }
        //getting sender and recipient
        UserRecord sender = senderOpt.get();
        UserRecord recipient = recipientOpt.get();
        //Imp condition
        if(transactionAmount>sender.getBalance()){
            return;
        }

        // CALL INCENTIVE API
        float incentiveAmount = incentiveService.fetchIncentive(transaction);


        //Updating the balance of sender and recipient
        sender.setBalance(sender.getBalance()-transactionAmount);
        recipient.setBalance(recipient.getBalance()+transactionAmount+incentiveAmount);

        //Saving info in DB
        userRepository.save(sender);
        userRepository.save(recipient);

        // Save transaction
        TransactionRecord record = new TransactionRecord( sender.getId(), recipient.getId(), transactionAmount,incentiveAmount);
        transactionRepository.save(record);
//        userRepository.findAll().forEach(user -> {
//            if(user.getName().equals("wilbur")){
//                System.out.println("wilbur>>>>>> balance = " + user.getBalance());
//            }
//        });
    }
}
