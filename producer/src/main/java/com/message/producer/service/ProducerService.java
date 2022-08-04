package com.message.producer.service;

import com.message.producer.apiresponse.ApiResponse;
import com.message.producer.dto.BookingDto;
import org.springframework.stereotype.Service;

@Service
public interface ProducerService {
    ApiResponse addBooking(BookingDto bookingDto);
    ApiResponse editBooking(BookingDto bookingDto);
    ApiResponse deleteBooking(String bookingId);
}
