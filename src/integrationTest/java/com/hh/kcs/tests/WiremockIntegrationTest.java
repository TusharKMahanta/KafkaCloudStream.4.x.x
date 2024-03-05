package com.hh.kcs.tests;

import com.hh.kcs.common.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class WiremockIntegrationTest extends BaseIntegrationTest {
    @Value("${hh.service.location.baseUrl}")
    private String locationBaseUrl;

    @Test
    public void wiremockTest(){
        WebClient webClient = WebClient.builder()
                .baseUrl(locationBaseUrl+"/locations/e22ee199-c8ae-47d0-a33e-2e715ee76ad7?fields=timeZone")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer token123")
                .build();

        String result=webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(e -> Mono.empty())
                .block();
        System.out.println(result);
    }
}
