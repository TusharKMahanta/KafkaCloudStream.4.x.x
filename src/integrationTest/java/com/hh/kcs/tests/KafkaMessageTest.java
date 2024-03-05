package com.hh.kcs.tests;

import com.hh.kcs.common.BaseIntegrationTest;
import com.hh.testingframwork.kafka.container.KafkaMessageContainer;
import com.hh.testingframwork.kafka.services.IKafkaTestContainerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.util.List;

public class KafkaMessageTest extends BaseIntegrationTest {
    @Autowired
    private IKafkaTestContainerService kafkaTestContainerService;

    @Autowired
    @Qualifier("kafkaMessageContainer")
    private KafkaMessageContainer<String> kafkaMessageContainer;

    @Test
    public void testKafkaMessage(){
        String message="Hello Integration Test";
        kafkaTestContainerService.publish("batch-out",message);
        Awaitility.await().until(()->{
            List<String> results= kafkaMessageContainer.filter(s-> s.equals(message));
           return results!=null && results.size()>0;
        });
        List<String> results= kafkaMessageContainer.filter(s-> s.equals(message));
        Assertions.assertEquals(results.get(0),message);
    }
}
