package com.hh.kcs.config;

import com.hh.kcs.mockbeans.MockEventProcessingService;
import com.hh.kcs.services.IEventProcessingService;
import com.hh.testingframwork.kafka.container.KafkaMessageContainer;
import com.hh.testingframwork.kafka.services.IKafkaTestContainerService;
import com.hh.testingframwork.kafka.services.KafkaTestContainerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.function.Consumer;

@TestConfiguration
@Slf4j
public class IntegrationTestConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String brokers;

    @Bean("eventProcessingService")
    public IEventProcessingService prepareEventProcessingService(){
        return new MockEventProcessingService();
    }

    @Bean
    public IKafkaTestContainerService prepareKafkaTestContainerService(){
        return new KafkaTestContainerService(brokers);
    }

    @Bean("kafkaMessageContainer")
    public KafkaMessageContainer<String> prepareKafkaMessageContainer(){
        return new KafkaMessageContainer<>();
    }

    @Bean
    public Consumer<String> integrationTestConsumerBinding() {
        KafkaMessageContainer<String> kafkaMessageContainer=prepareKafkaMessageContainer();
        return s -> {
            kafkaMessageContainer.add(s);
            log.info(" integrationTestConsumerBinding batch-out -> " + s);
        };
    }
}
