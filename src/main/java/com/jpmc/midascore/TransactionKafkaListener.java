package com.jpmc.midascore;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TransactionKafkaListener {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final RestTemplate restTemplate;

    public TransactionKafkaListener(UserRepository userRepository,
                                    RestTemplate restTemplate,
                                    TransactionRepository transactionRepository

    ){
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.restTemplate = restTemplate;
    }

    @KafkaListener(
            topics = "${general.kafka-topic}",
            groupId = "midas-group",
            properties = {"auto.offset.reset=earliest"}
    )
    @Transactional
    public void consume(Transaction transaction) {

        // Get Sender (UserRecord)
        long senderId = transaction.getSenderId();
        UserRecord sender = userRepository.findById(senderId);

        // Get Recipient (UserRecord)
        long recipientId = transaction.getRecipientId();
        UserRecord receiver = userRepository.findById(recipientId);

        // Check both exists
        if (sender == null || receiver == null) {
            System.out.println("Invalid Transaction");
            return;
        }

        float amount = transaction.getAmount();
        float senderBalance = sender.getBalance();

        if (senderBalance < amount) {
            System.out.println("Insufficient Balance");
            return;
        }
        System.out.println("Transaction -> "+ transaction);
        System.out.println(" Sender ID : "+ transaction.getSenderId());
        System.out.println(" Receiver ID : "+ transaction.getRecipientId());
        System.out.println(" Amount : "+ transaction.getAmount());

        Incentive incentive = restTemplate.postForObject(
                "http://localhost:8080/incentive",
                transaction,
                Incentive.class
        );

        System.out.println("Incentive response: " + incentive);

        float incentiveAmount = 0f;

        if(incentive != null){
            incentiveAmount = incentive.getAmount();
            System.out.println("Incentive Amount -> "+incentiveAmount);
        }

        sender.setBalance(senderBalance - amount);
        receiver.setBalance(receiver.getBalance() + amount + incentiveAmount);

        //Create TransactionRecord Object
        TransactionRecord transactionRecord = new TransactionRecord(sender, receiver, amount, incentiveAmount);

        userRepository.save(sender);
        userRepository.save(receiver);

        transactionRepository.save(transactionRecord);

        System.out.println("----After Transaction ---- ");
        System.out.println(sender);
        System.out.println(receiver);
        System.out.println(transactionRecord);
        System.out.println("----------------------------");

    }
}
