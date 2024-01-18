package com.hh.kcs.services;

public interface IKafkaEventProcessingService {
    void processEvent(String event);
}
