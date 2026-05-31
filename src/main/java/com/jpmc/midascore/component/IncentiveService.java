package com.jpmc.midascore.component;

import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class IncentiveService {

    private final RestTemplate restTemplate;

    public IncentiveService() {
        this.restTemplate = new RestTemplate();
    }

    public Incentive getIncentive(Transaction transaction) {
        try {
            Incentive incentive = restTemplate.postForObject(
                    "http://localhost:8080/incentive",
                    transaction,
                    Incentive.class
            );
            return incentive != null ? incentive : new Incentive();
        } catch (Exception e) {
            // If API is unavailable, return zero incentive
            return new Incentive();
        }
    }
}
