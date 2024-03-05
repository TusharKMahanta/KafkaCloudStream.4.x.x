package com.hh.kcs.common;

import com.hh.kcs.config.IntegrationTestConfig;
import com.hh.testingframwork.couchbase.CouchbaseStartupExtension;
import com.hh.testingframwork.kafka.KafkaStartupExtension;
import com.hh.testingframwork.wiremock.WireMockStartupExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(IntegrationTestConfig.class)
@ActiveProfiles("integrationTest")
public class BaseIntegrationTest {
    @RegisterExtension
    static CouchbaseStartupExtension couchbaseStartupExtension = new CouchbaseStartupExtension("hhbucket");

    @RegisterExtension
    static KafkaStartupExtension kafkaStartupExtension=new KafkaStartupExtension();

    @RegisterExtension
    static WireMockStartupExtension wireMockStartupExtension=new WireMockStartupExtension("stubs");


}
