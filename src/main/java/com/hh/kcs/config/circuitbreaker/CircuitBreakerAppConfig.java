package com.hh.kcs.config.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerOnStateTransitionEvent;
import io.github.resilience4j.core.registry.EntryAddedEvent;
import io.github.resilience4j.core.registry.EntryRemovedEvent;
import io.github.resilience4j.core.registry.EntryReplacedEvent;
import io.github.resilience4j.core.registry.RegistryEventConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binding.BindingsLifecycleController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Configuration
public class CircuitBreakerAppConfig {
    @Autowired
    private BindingsLifecycleController bindingsController;

    @Bean
    public CircuitBreakerRegistry prepareCircuitBreakerRegistry() {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofMillis(10000))
                .permittedNumberOfCallsInHalfOpenState(2)
                .slidingWindowSize(10)
                //.recordExceptions(IOException.class, TimeoutException.class)
                //.ignoreExceptions(BusinessException.class, OtherBusinessException.class)
                .build();

        // Create a CircuitBreakerRegistry with a custom global configuration
        CircuitBreakerRegistry circuitBreakerRegistry =
                CircuitBreakerRegistry.custom()
                        .withCircuitBreakerConfig(circuitBreakerConfig)
                        .addRegistryEventConsumer(prepareRegistryEventConsumer())
                        .build();
        return circuitBreakerRegistry;
    }

    private RegistryEventConsumer prepareRegistryEventConsumer() {
        return new RegistryEventConsumer<CircuitBreaker>() {
            @Override
            public void onEntryAddedEvent(EntryAddedEvent<CircuitBreaker> entryAddedEvent) {
                entryAddedEvent.getAddedEntry().getEventPublisher().onStateTransition(this::onStateTransition);
            }

            @Override
            public void onEntryRemovedEvent(EntryRemovedEvent<CircuitBreaker> entryRemoveEvent) {
                entryRemoveEvent.getRemovedEntry().getEventPublisher().onStateTransition(this::onStateTransition);
            }

            @Override
            public void onEntryReplacedEvent(EntryReplacedEvent<CircuitBreaker> entryReplacedEvent) {
                entryReplacedEvent.getNewEntry().getEventPublisher().onStateTransition(this::onStateTransition);
            }

            private void onStateTransition(CircuitBreakerOnStateTransitionEvent circuitBreakerEvent) {
                CircuitBreaker.State toState = circuitBreakerEvent.getStateTransition()
                        .getToState();
                String binderName = circuitBreakerEvent.getCircuitBreakerName();
                bindingsController.queryState(binderName).forEach(binding -> {
                    switch (toState) {
                        case OPEN -> {
                            //log.info("Pausing the consumer for binder {}", binderName);
                            bindingsController.pause(binderName);
                            break;
                        }
                        case CLOSED, HALF_OPEN -> {
                            //log.info("Resuming the consumer for binder {}", binderName);
                            bindingsController.resume(binderName);
                            break;
                        }
                    }
                });

            }
        };
    }
}
