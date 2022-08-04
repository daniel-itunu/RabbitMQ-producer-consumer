package com.message.producer.model;

import lombok.Data;

@Data
public class TripWayPoint {
    private Long id;
    private String locality;
    private String latitude;
    private String longitude;
}
