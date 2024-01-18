package com.hh.kcs;

import com.hh.kcs.services.EventProcessingService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class KafkaCloudStreamApplicationIntegrationTests {
	@Test
	void contextLoadIntegrationTest() {
		//EventProcessingService eventProcessingService=new EventProcessingService();
		//eventProcessingService.processEvent("Hello Tushar");
		log.info("Hello Integration Test !!!!!");
	}
}
