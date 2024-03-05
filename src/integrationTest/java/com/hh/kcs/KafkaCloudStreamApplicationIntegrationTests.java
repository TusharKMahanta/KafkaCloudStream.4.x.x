package com.hh.kcs;

import com.hh.kcs.common.BaseIntegrationTest;
import com.hh.kcs.services.EventProcessingService;
import com.hh.kcs.services.KafkaEventProcessingService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class KafkaCloudStreamApplicationIntegrationTests extends BaseIntegrationTest {



	@Test
	void contextLoadIntegrationTest() {
		EventProcessingService eventProcessingService=new EventProcessingService();
		eventProcessingService.processEvent("Hello Tushar");
		KafkaEventProcessingService kafkaEventProcessingService=new KafkaEventProcessingService(eventProcessingService);
		kafkaEventProcessingService.processEvent("Hello Tushar");
		kafkaEventProcessingService.logMessage("Hello Tushar");
		kafkaEventProcessingService.logMessage1("Hello Tushar");
		log.info("Hello Integration Test !!!!!");
	}
}
