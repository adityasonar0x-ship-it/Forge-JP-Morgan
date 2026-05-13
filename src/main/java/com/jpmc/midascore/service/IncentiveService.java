package com.jpmc.midascore.service;

import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.foundation.Incentive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class IncentiveService {

    @Autowired
    private RestTemplate restTemplate;

    private final String url = "http://localhost:8080/incentive";

    public float fetchIncentive(Transaction transaction){

        Incentive response =
                restTemplate.postForObject(url, transaction, Incentive.class);

        if(response == null){
            return 0f;
        }

        return response.getIncentiveAmount();
    }
}