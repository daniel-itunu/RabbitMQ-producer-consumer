package com.message.consumer.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "TripWayPoint")
@Table(name = "tripWayPoint")
public class TripWayPoint {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String tripWayPointId;
    private String locality;
    private String latitude;
    private String longitude;
    @JoinColumn(name = "Booking", referencedColumnName = "id")
    @ManyToOne
    private Booking booking;
}
