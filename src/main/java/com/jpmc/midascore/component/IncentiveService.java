package com.jpmc.midascore.component;

import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class IncentiveService {

    @Value("${general.incentive-endpoint}")
    private String incentiveEndpoint;

    private final RestTemplate restTemplate = new RestTemplate();

    public float getIncentive(Transaction transaction) {
        try {
            Incentive incentive = restTemplate.postForObject(
                incentiveEndpoint,
                transaction,
                Incentive.class
            );
            return incentive != null ? incentive.getAmount() : 0f;
        } catch (Exception e) {
            return 0f;
        }
    }
}