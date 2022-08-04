package com.message.producer.controller;

import com.message.producer.apiresponse.ApiResponse;
import com.message.producer.dto.BookingDto;
import com.message.producer.service.ProducerService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProducerController {

    private final ProducerService producerService;

    public ProducerController(ProducerService producerService) {
        this.producerService = producerService;
    }

    @PostMapping("/booking")
    public ResponseEntity<ApiResponse> addBooking(@Validated(BookingDto.Validation.class) @RequestBody BookingDto bookingDto){
        ApiResponse apiResponse = producerService.addBooking(bookingDto);
        return new ResponseEntity<>(apiResponse, apiResponse.getHttpStatus());
    }

    @PutMapping("/booking")
    public ResponseEntity<ApiResponse> editBooking(@Validated(BookingDto.Validation.class) @RequestBody BookingDto bookingDto){
        ApiResponse apiResponse = producerService.editBooking(bookingDto);
        return new ResponseEntity<>(apiResponse, apiResponse.getHttpStatus());
    }

    @DeleteMapping("/booking/{bookingId}")
    public ResponseEntity<ApiResponse> deleteBooking(@PathVariable("bookingId") String bookingId){
        ApiResponse apiResponse = producerService.deleteBooking(bookingId);
        return new ResponseEntity<>(apiResponse, apiResponse.getHttpStatus());
    }
}
