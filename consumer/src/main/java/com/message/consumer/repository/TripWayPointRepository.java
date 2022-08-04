package com.message.consumer.repository;

import com.message.consumer.model.TripWayPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripWayPointRepository extends JpaRepository<TripWayPoint, Long> {
    TripWayPoint findTripWayPointByTripWayPointId(String tripWayPointId);

}
