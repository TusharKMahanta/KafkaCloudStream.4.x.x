package com.hh.kcs.mockbeans;

import com.hh.kcs.services.IEventProcessingService;
import io.micrometer.tracing.annotation.SpanTag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MockEventProcessingService implements IEventProcessingService {
    public void processEvent(String event){
        log.info("MockEventProcessingService -> " + event);
    }
}
