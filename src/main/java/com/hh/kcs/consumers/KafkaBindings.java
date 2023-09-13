package com.hh.kcs.consumers;

import jakarta.annotation.Nullable;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.cloud.stream.binder.BinderCustomizer;
import org.springframework.cloud.stream.binder.kafka.KafkaMessageChannelBinder;
import org.springframework.cloud.stream.binder.kafka.ListenerContainerWithDlqAndRetryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.BackOff;

import java.util.function.BiFunction;
import java.util.function.Consumer;

@Configuration
public class KafkaBindings {

    @Bean
    public Consumer<String> consumerBinding() {
        return s -> System.out.println("batch-out -> " + s);
    }

    @Bean
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

}
