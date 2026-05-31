package com.jpmc.midascore;

import com.jpmc.midascore.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@TestPropertySource(properties = {
        "general.kafka-topic=trader-updates",
        "spring.datasource.url=jdbc:h2:mem:midasdb",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer",
        "spring.kafka.consumer.auto-offset-reset=earliest",
        "spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.JsonDeserializer",
        "spring.kafka.consumer.properties.spring.json.trusted.packages=*"
})
public class TaskThreeTests {
    static final Logger logger = LoggerFactory.getLogger(TaskThreeTests.class);

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private UserPopulator userPopulator;

    @Autowired
    private FileLoader fileLoader;

    @Autowired
    private UserRepository userRepository;

    @Test
    void task_three_verifier() throws InterruptedException {
        userPopulator.populate();
        String[] transactionLines = fileLoader.loadStrings("/test_data/mnbvcxz.vbnm");
        for (String transactionLine : transactionLines) {
            kafkaProducer.send(transactionLine);
        }
        Thread.sleep(10000);

        var waldorf = userRepository.findById(5);
        float balance = waldorf.getBalance();
        int answer = (int) balance;

        // Write to file so it can't be lost in IntelliJ's console buffer
        try {
            java.nio.file.Files.writeString(
                java.nio.file.Path.of("WALDORF_ANSWER.txt"),
                "WALDORF FINAL BALANCE = " + balance + "\n" +
                "WALDORF FLOOR ANSWER  = " + answer + "\n"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Use System.err so it appears in a different color and bypasses log buffering
        System.err.println("\n\n");
        System.err.println("===========================================================");
        System.err.println("WALDORF FINAL BALANCE = " + balance);
        System.err.println("WALDORF FLOOR ANSWER  = " + answer);
        System.err.println("===========================================================");
        System.err.println("\n\n");
    }
}
