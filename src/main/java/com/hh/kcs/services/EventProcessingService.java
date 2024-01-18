package com.hh.kcs.services;

import io.micrometer.tracing.annotation.NewSpan;
import io.micrometer.tracing.annotation.SpanTag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EventProcessingService implements IEventProcessingService{
    //@NewSpan
    public void processEvent( @SpanTag("id")String event){
        log.info("EventProcessingService -> " + event);
    }
}
