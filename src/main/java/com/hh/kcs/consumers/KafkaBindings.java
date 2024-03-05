package com.hh.kcs.consumers;

import com.hh.kcs.config.circuitbreaker.PauseOnFailure;
import com.hh.kcs.services.IKafkaEventProcessingService;
import io.micrometer.tracing.annotation.NewSpan;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Configuration
@Slf4j
public class KafkaBindings {
    @Autowired
    private IKafkaEventProcessingService kafkaEventProcessingService;

    @Bean
    @PauseOnFailure
    public Consumer<String> consumerBinding() {
        return s -> log.info("batch-out -> " + s);
    }

    @Bean
    @PauseOnFailure
    @NewSpan
    public Consumer<String> consumerBinding1() {
        return s ->{
                UUID uuid=UUID.randomUUID();
                MDC.put("traceId",uuid.toString());
                log.info("KafkaBindings batch-out -> " + s+" Trace Id::"+ uuid.toString());
                kafkaEventProcessingService.processEvent(s);
        };
       /* s -> {
                log.info("batch-out -> " + s);
                //throw new RuntimeException();
            };*/
        //}
    }

    @Bean
    public Consumer<String> dlqBinding() {
        return s -> {
            log.info("Dead Letter Message -> " + s);
        };
    }

    @Bean
    public Function<String, String> processorBinding() {
        return s -> s + " :: " + System.currentTimeMillis();
    }

    @Bean
    public Supplier<String> producerBinding() {
        return () -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "{id:'Tushar'}";
        };
    }
}
