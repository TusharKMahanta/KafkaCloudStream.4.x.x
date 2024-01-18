package com.hh.kcs.services;

import io.micrometer.tracing.annotation.NewSpan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaEventProcessingService implements IKafkaEventProcessingService {
    @Autowired
    IEventProcessingService eventProcessingService;
    @NewSpan
    public void processEvent(String event){
        log.info("KafkaEventProcessingService -> " + event);
        logMessage(event);
    }
    public void logMessage(String event){
        log.info("KafkaEventProcessingService Log 1 -> " + event);
        logMessage1(event);
    }

    @NewSpan
    public void logMessage1(String event){
        log.info("KafkaEventProcessingService Log 2 -> " + event);
        for (int i = 0; i < 3; i++) {
            eventProcessingService.processEvent(event);
        }
        log.info("KafkaEventProcessingService Log 2 End-> " + event);
    }
}
