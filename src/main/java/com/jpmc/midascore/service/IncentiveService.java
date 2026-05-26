package com.jpmc.midascore.service;

import com.jpmc.midascore.foundation.Incentive;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.jpmc.midascore.foundation.Transaction;

@Service
public class IncentiveService {

    private static final String INCENTIVE_API = "http://localhost:8080/incentive";

    private final RestTemplate restTemplate;

    public IncentiveService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public float getIncentive(Transaction transaction) {
        try {
            Incentive incentive = restTemplate.postForObject(INCENTIVE_API, transaction, Incentive.class);
            if (incentive != null) {
                System.out.println("Received incentive:" + incentive + "for transaction:" + transaction);
                return incentive.getAmount();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return 0f;
    }

}