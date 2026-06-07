package com.jpmc.midascore.repository;

import com.jpmc.midascore.entity.TransactionRecord;
import jakarta.persistence.OneToMany;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRecordRepository
        extends JpaRepository<TransactionRecord, Long> {

}