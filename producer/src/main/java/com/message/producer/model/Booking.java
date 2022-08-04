package com.message.producer.model;

import com.message.producer.dto.TripWayPointDto;
import lombok.Data;

import java.util.List;

@Data
public class Booking {

    private Long id;
    private String passengerName;
    private String contactNumber;
    private String pickupTime;
    private String asap;
    private String waitingTime;
    private String numberOfPassengers;
    private String price;
    private String rating;
    private String createdOn;
    private String lastModifiedOn;
    private List<TripWayPointDto> tripWayPointDtos;
}
