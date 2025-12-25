package com.jpmc.midascore;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

/**
 * Task Five requires an external REST service running at:
 * http://localhost:33400/balance
 *
 * Since this service is NOT available in the Forge/JPMC environment,
 * this test is intentionally disabled.
 */
@Disabled
@SpringBootTest
public class TaskFiveTests {

    private static final Logger logger =
            LoggerFactory.getLogger(TaskFiveTests.class);

    @Autowired
    private RestTemplate restTemplate;

    @Test
    void task_five_verifier() {
        logger.info("Task Five test is disabled because it depends on an external REST service.");
    }
}

