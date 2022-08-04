package com.message.consumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

@Service
public interface ConsumerService {
    void addBooking(String bookingDto) throws JsonProcessingException;
    void editBooking(String bookingDto) throws JsonProcessingException;
    void deleteBooking(String id) throws JsonProcessingException;
    void auditMessage(String message) throws JsonProcessingException;
}
