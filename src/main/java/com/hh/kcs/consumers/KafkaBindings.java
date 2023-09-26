package com.hh.kcs.consumers;

import com.hh.kcs.config.circuitbreaker.PauseOnFailure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Configuration
@Slf4j
public class KafkaBindings {

    @Bean
    @PauseOnFailure
    public Consumer<String> consumerBinding() {
        log.info("Hello Tushar");
        return s -> System.out.println("batch-out -> " + s);
    }

    @Bean
    @PauseOnFailure
    public Consumer<String> consumerBinding1() {
        return s -> {
            //System.out.println("batch-out -> " + s);
            throw new RuntimeException();
        };
    }

    @Bean
    public Consumer<String> dlqBinding() {
        return s -> {
            System.out.println("Dead Letter Message -> " + s);
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
            return "new data";
        };
    }
}
