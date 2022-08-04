package com.message.consumer.repository;

import com.message.consumer.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Booking findBookingByBookingId(String bookingId);
}
