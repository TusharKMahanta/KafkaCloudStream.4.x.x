package com.hh.kcs.config;

import jakarta.annotation.Nullable;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.binder.BinderCustomizer;
import org.springframework.cloud.stream.binder.kafka.KafkaMessageChannelBinder;
import org.springframework.cloud.stream.binder.kafka.ListenerContainerWithDlqAndRetryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.BackOff;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

@Configuration
public class KafkaDlqConfig {
    @Value("${dlq.kafka.topic}")
    String dlqTopic;
    @Value("${dlq.kafka.brokers}")
    String dlqBrokers;
   /* @Value("${dlq.kafka.backoff}")
    private String reconnectBackoff;
    @Value("${dlq.kafka.backoffMax}")
    private String reconnectBackOffMax;*/
    @Value("${dlq.kafka.acks:all}")
    private String acks;
    @Value("${dlq.kafka.idempotence:true}")
    private String idempotenceEnabled;
    @Value("${dlq.kafka.maxRequestsPerConnection:1}")
    private String maxRequestsPerConnection;
    @Value("${dlq.kafka.retries:3}")
    private String retries;
    @Value("${dlq.kafka.deliveryTimeoutMs:3000}")
    public String deliveryTimeoutMs;
    @Value("${dlq.kafka.requestTimeout:3000}")
    public String requestTimeoutMs;
    @Value("${dlq.kafka.securityProtocol:PLAINTEXT}")
    private String securityProtocol;
    @Value("${dlq.kafka.sslTruststoreLocation:none}")
    private String sslTruststoreLocation;
    @Value("${dlq.kafka.sslTruststorePassword:none}")
    private String sslTruststorePassword;
    @Value("${dlq.kafka.saslMechanism:none}")
    private String saslMechanism;
    @Value("${dlq.kafka.saslJaasConfig:none}")
    private String saslJaasConfig;

    @Value("${dlq.kafka.sslEndpointIdentificationAlgorithm:none}")
    private String sslEndpointIdentificationAlgorithm;
    private static final String PLAINTEXT = "PLAINTEXT";
    private static final int RANDOM_PARTITION = -1;

    @Bean
    public ListenerContainerWithDlqAndRetryCustomizer cust() {
        ConsumerRecordRecoverer consumerRecordRecoverer = prepareConsumerRecordRecoverer();
        return new ListenerContainerWithDlqAndRetryCustomizer() {

            @Override
            public void configure(AbstractMessageListenerContainer<?, ?> container, String destinationName,
                                  String group,
                                  @Nullable BiFunction<ConsumerRecord<?, ?>, Exception, TopicPartition> dlqDestinationResolver,
                                  @Nullable BackOff backOff) {
                container.setCommonErrorHandler(new DefaultErrorHandler(consumerRecordRecoverer, backOff));
            }

            @Override
            public boolean retryAndDlqInBinding(String destinationName, String group) {
                return true;
            }

        };
    }

    @Bean
    public BinderCustomizer binderCustomizer(ListenerContainerWithDlqAndRetryCustomizer containerCustomizer) {
        return (binder, binderName) -> {
            if (KafkaMessageChannelBinder.class.isInstance(binder)) {
                KafkaMessageChannelBinder kafkaMessageChannelBinder = KafkaMessageChannelBinder.class.cast(binder);
                kafkaMessageChannelBinder.setContainerCustomizer(containerCustomizer);
            }
        };
    }

    private ConsumerRecordRecoverer prepareConsumerRecordRecoverer() {
        KafkaTemplate<String, String> template = prepareDlqKafkaTemplate();
        return new DeadLetterPublishingRecoverer(template,
                (destinationTopic, partition) -> new TopicPartition(dlqTopic, RANDOM_PARTITION)) {

            @Override
            protected ProducerRecord<Object, Object> createProducerRecord(
                    ConsumerRecord<?, ?> record,
                    TopicPartition topicPartition,
                    Headers headers,
                    @Nullable byte[] key,
                    @Nullable byte[] value) {
                return super.createProducerRecord(record, topicPartition, headers, key, value);
            }
        };
    }

    private KafkaTemplate<String, String> prepareDlqKafkaTemplate() {
        return new KafkaTemplate(new DefaultKafkaProducerFactory<>(prepareDlqProducerConfigs()));
    }

    Map<String, Object> prepareDlqProducerConfigs() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, dlqBrokers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        /*configProps.put(
                ProducerConfig.RECONNECT_BACKOFF_MS_CONFIG, reconnectBackoff);
        configProps.put(
                ProducerConfig.RECONNECT_BACKOFF_MAX_MS_CONFIG, reconnectBackOffMax);*/
        configProps.put(ProducerConfig.ACKS_CONFIG, acks);
        configProps.put(
                ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, idempotenceEnabled);
        configProps.put(
                ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION,maxRequestsPerConnection);
        configProps.put(ProducerConfig.RETRIES_CONFIG, retries);
        configProps.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, deliveryTimeoutMs);
        configProps.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, requestTimeoutMs);

        if (!PLAINTEXT.equals(securityProtocol)) {
            configProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, securityProtocol);
            configProps.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, sslTruststoreLocation);
            configProps.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, sslTruststorePassword);
            configProps.put(SaslConfigs.SASL_MECHANISM, saslMechanism);
            configProps.put(SaslConfigs.SASL_JAAS_CONFIG, saslJaasConfig);
            configProps.put(
                    SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG,
                    sslEndpointIdentificationAlgorithm);
        }
        return configProps;
    }
}
