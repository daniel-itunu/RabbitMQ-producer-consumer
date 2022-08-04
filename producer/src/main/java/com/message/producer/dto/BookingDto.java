package com.message.producer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class BookingDto {
    @NotBlank(message = "bookingId is required", groups = BookingDto.Validation.class)
    @NotEmpty(message = "bookingId is required", groups = BookingDto.Validation.class)
    @NotNull(message = "bookingId is required", groups = BookingDto.Validation.class)
    private String bookingId;
    @NotBlank(message = "passengerName is required", groups = BookingDto.Validation.class)
    @NotEmpty(message = "passengerName is required", groups = BookingDto.Validation.class)
    @NotNull(message = "passengerName is required", groups = BookingDto.Validation.class)
    private String passengerName;
    private String contactNumber;
    private String pickupTime;
    private String asap;
    private String waitingTime;
    private String numberOfPassengers;
    private String price;
    private String rating;
    private List<TripWayPointDto> tripWayPoints;

    public interface Validation{}
}
