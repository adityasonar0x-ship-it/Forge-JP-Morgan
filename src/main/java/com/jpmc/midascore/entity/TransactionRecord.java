package com.jpmc.midascore.entity;
import jakarta.persistence.*;

@Entity
public class TransactionRecord {
    @Id
    @GeneratedValue()
    private long id;

    @ManyToOne
    private UserRecord sender;

    @ManyToOne
    private UserRecord reciever;

    @Column(nullable = false)
    private float amount;

    @Column
    private float incentive;

    protected TransactionRecord() {
    }

    public TransactionRecord(UserRecord sender, UserRecord reciever, float amount, float incentive) {
        this.sender = sender;
        this.reciever = reciever;
        this.amount = amount;
        this.incentive = incentive;
    }

    @Override
    public String toString() {
        return String.format("Transaction Record [id=%d, sender='%s', recieverId='%s', amount='%f', incentive='%f' ", id, sender,reciever, amount, incentive);
    }

    public Long getId() {
        return id;
    }

    public UserRecord getSender() {
        return sender;
    }

    public UserRecord getReciever() {
        return reciever;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setIncentive(float amount){ this.incentive = amount; }

}
