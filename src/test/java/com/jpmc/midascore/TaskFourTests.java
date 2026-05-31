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
public class TaskFourTests {
    static final Logger logger = LoggerFactory.getLogger(TaskFourTests.class);

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private UserPopulator userPopulator;

    @Autowired
    private FileLoader fileLoader;

    @Autowired
    private UserRepository userRepository;

    @Test
    void task_four_verifier() throws InterruptedException {
        userPopulator.populate();
        String[] transactionLines = fileLoader.loadStrings("/test_data/alskdjfh.fhdjsk");
        for (String transactionLine : transactionLines) {
            kafkaProducer.send(transactionLine);
        }
        Thread.sleep(10000);

        var wilbur = userRepository.findById(9); // wilbur is id=9
        float balance = wilbur != null ? wilbur.getBalance() : -1;
        int answer = (int) balance;

        // Write to file (bypasses IntelliJ console buffer)
        try {
            java.nio.file.Files.writeString(
                java.nio.file.Path.of("WILBUR_ANSWER.txt"),
                "WILBUR FINAL BALANCE = " + balance + "\n" +
                "WILBUR FLOOR ANSWER  = " + answer + "\n"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.err.println("\n===========================================================");
        System.err.println("WILBUR FINAL BALANCE = " + balance);
        System.err.println("WILBUR FLOOR ANSWER  = " + answer);
        System.err.println("===========================================================\n");
    }
}
