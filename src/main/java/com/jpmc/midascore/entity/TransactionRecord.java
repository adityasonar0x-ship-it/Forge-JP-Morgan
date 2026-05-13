package com.jpmc.midascore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TransactionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long senderId;
    private long recipientId;
    private float transactionAmount;
    private float incentiveAmount;

    public TransactionRecord( long senderId, long recipientId, float transactionAmount,  float incentiveAmount ) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.transactionAmount = transactionAmount;
        this.incentiveAmount = incentiveAmount;
    }


}
