package com.message.consumer.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity(name = "Booking")
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String bookingId;
    private String passengerName;
    private String contactNumber;
    private String pickupTime;
    private String asap;
    private String waitingTime;
    private String numberOfPassengers;
    private String price;
    private String rating;
    private LocalDateTime createdOn;
    private LocalDateTime lastModifiedOn;
    @OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    private List<TripWayPoint> tripWayPoints;
}
