package com.jpmc.midascore.component;

import com.jpmc.midascore.foundation.Transaction;
import org.apache.kafka.clients.producer.MockProducer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public ProducerFactory<String, Transaction> producerFactory() {
        return () -> new MockProducer<>(
                true,
                new StringSerializer(),
                new JsonSerializer<>()
        );
    }

    @Bean
    public KafkaTemplate<String, Transaction> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}

