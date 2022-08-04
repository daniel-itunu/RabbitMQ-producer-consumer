package com.message.producer.dto;

import lombok.Data;

@Data
public class TripWayPointDto {
    private String tripWayPointId;
    private String locality;
    private String latitude;
    private String longitude;
}
