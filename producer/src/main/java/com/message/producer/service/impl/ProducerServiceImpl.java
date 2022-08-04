package com.message.producer.service.impl;

import com.message.producer.apiresponse.ApiResponse;
import com.message.producer.configuration.SystemProperty;
import com.message.producer.dto.BookingDto;
import com.message.producer.service.ProducerService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ProducerServiceImpl implements ProducerService {
private final RabbitTemplate rabbitTemplate;
private final SystemProperty systemProperty;

    public ProducerServiceImpl(RabbitTemplate rabbitTemplate, SystemProperty systemProperty) {
        this.rabbitTemplate = rabbitTemplate;
        this.systemProperty = systemProperty;
    }

    @Override
    public ApiResponse addBooking(BookingDto bookingDto) {
        rabbitTemplate.setRoutingKey(systemProperty.getAddBookingQueueKey());
        rabbitTemplate.convertAndSend(bookingDto);
        ApiResponse apiResponse = new ApiResponse("Success", "1",
                "add-booking published successfully", null, HttpStatus.OK);
        return apiResponse;
    }

    @Override
    public ApiResponse editBooking(BookingDto bookingDto) {
        rabbitTemplate.setRoutingKey(systemProperty.getEditBookingQueueKey());
        rabbitTemplate.convertAndSend(bookingDto);
        ApiResponse apiResponse = new ApiResponse("Success", "1",
                "edit-booking published successfully", null, HttpStatus.OK);
        return apiResponse;
    }

    @Override
    public ApiResponse deleteBooking(String bookingId) {
        ApiResponse apiResponse;
        if(null==bookingId||bookingId.isEmpty()||bookingId.isBlank()){
          apiResponse = new ApiResponse("Failed", "0",
                    "Booking id is required",null, HttpStatus.BAD_REQUEST);
            return apiResponse;
        }
        rabbitTemplate.setRoutingKey(systemProperty.getDeleteBookingQueueKey());
        rabbitTemplate.convertAndSend(bookingId);
         apiResponse = new ApiResponse("Success", "1",
                "delete-booking published successfully", null, HttpStatus.OK);
        return apiResponse;
    }
}
