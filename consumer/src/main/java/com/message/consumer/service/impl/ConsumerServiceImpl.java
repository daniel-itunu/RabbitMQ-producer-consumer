package com.message.consumer.service.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.message.consumer.model.Booking;
import com.message.consumer.model.TripWayPoint;
import com.message.consumer.repository.BookingRepository;
import com.message.consumer.repository.TripWayPointRepository;
import com.message.consumer.service.ConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ConsumerServiceImpl implements ConsumerService {
    private final BookingRepository bookingRepository;
    private final TripWayPointRepository tripWayPointRepository;
    private final ObjectMapper objectMapper;
    private final String ADD_BOOKING_QUEUE_NAME = "add.booking.queue";
    private final String EDIT_BOOKING_QUEUE_NAME = "edit.booking.queue";
    private final String DELETE_BOOKING_QUEUE_NAME = "delete.booking.queue";
    private final String AUDIT_MESSAGE_QUEUE_NAME = "message.audit.queue";

    public ConsumerServiceImpl(BookingRepository bookingRepository, TripWayPointRepository tripWayPointRepository, ObjectMapper objectMapper) {
        this.bookingRepository = bookingRepository;
        this.tripWayPointRepository = tripWayPointRepository;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = ADD_BOOKING_QUEUE_NAME)
    @Override
    public void addBooking(String bookingDto) {
        try {
            Booking booking = objectMapper.readValue(bookingDto, Booking.class);
            booking.setCreatedOn(LocalDateTime.now());
            bookingRepository.save(booking);
            log.info("addBooking response -> {}", "request saved successfully");
        } catch (Exception e) {
            log.info("addBooking Exception -> {}", e.getMessage());
        }
    }

    @RabbitListener(queues = EDIT_BOOKING_QUEUE_NAME)
    @Override
    public void editBooking(String bookingDto) {
        try {
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            Booking booking = objectMapper.readValue(bookingDto, Booking.class);
            String bookingId = booking.getBookingId();
            if (null == bookingId) {
                log.info("editBooking response -> {}", "bookingId is null, bookingId is required to make any edit");
                return;
            }
            Booking foundBooking = bookingRepository.findBookingByBookingId(bookingId);
            if (null == foundBooking) {
                log.info("editBooking response -> {}", "booking with id " + bookingId + " not found");
                return;
            }
            if (null != booking.getAsap()) {
                foundBooking.setAsap(booking.getAsap());
            }
            if (null != booking.getContactNumber()) {
                foundBooking.setContactNumber(booking.getContactNumber());
            }
            foundBooking.setLastModifiedOn(LocalDateTime.now());
            if (null != booking.getPassengerName()) {
                foundBooking.setPassengerName(booking.getPassengerName());
            }
            if (null != booking.getPickupTime()) {
                foundBooking.setPickupTime(booking.getPickupTime());
            }
            if (null != booking.getPrice()) {
                foundBooking.setPrice(booking.getPrice());
            }
            if (null != booking.getRating()) {
                foundBooking.setRating(booking.getRating());
            }
            if (null != booking.getNumberOfPassengers()) {
                foundBooking.setNumberOfPassengers(booking.getNumberOfPassengers());
            }
            if (null != booking.getWaitingTime()) {
                foundBooking.setWaitingTime(booking.getWaitingTime());
            }
            List<TripWayPoint> foundTripWayPointList = booking.getTripWayPoints();
            List<TripWayPoint> tripWayPointsUpdated = new ArrayList<>();
            if (foundTripWayPointList.size() != 0) {
                foundTripWayPointList.stream().forEach(tripWayPoint -> {
                    if (null != tripWayPoint.getTripWayPointId()) {
                        TripWayPoint foundTripWayPoint = tripWayPointRepository.findTripWayPointByTripWayPointId(tripWayPoint.getTripWayPointId());
                        if(null!=foundTripWayPoint){
                            if (null != tripWayPoint.getLocality()) {
                                foundTripWayPoint.setLocality(tripWayPoint.getLocality());
                            }
                            if (null != tripWayPoint.getLatitude()) {
                                foundTripWayPoint.setLatitude(tripWayPoint.getLatitude());
                            }
                            if (null != tripWayPoint.getLongitude()) {
                                foundTripWayPoint.setLongitude(tripWayPoint.getLongitude());
                            }
                            tripWayPointsUpdated.add(foundTripWayPoint);
                        }else {
                            TripWayPoint newTripWayPoint = new TripWayPoint();
                            newTripWayPoint.setTripWayPointId(tripWayPoint.getTripWayPointId());
                            newTripWayPoint.setLocality(tripWayPoint.getLocality());
                            newTripWayPoint.setLatitude(tripWayPoint.getLatitude());
                            newTripWayPoint.setLongitude(tripWayPoint.getLongitude());
                            tripWayPointsUpdated.add(newTripWayPoint);
                        }
                    }
                });
            }
            foundBooking.setTripWayPoints(tripWayPointsUpdated);
            bookingRepository.save(foundBooking);
            log.info("editBooking response-> {}", "booking with id " + booking.getBookingId() + " edited successfully");
        } catch (Exception e) {
            log.info("editBooking Exception -> {}", e.getMessage());
        }
    }

    @RabbitListener(queues = DELETE_BOOKING_QUEUE_NAME)
    @Override
    public void deleteBooking(String bookingId) {
        try {
            String id = objectMapper.readValue(bookingId, String.class);
            Booking booking = bookingRepository.findBookingByBookingId(id);
            if (null != booking) {
                bookingRepository.delete(booking);
                log.info("deleteBooking response -> {}", "booking with id " + id + " deleted successfully");
                return;
            }
            log.info("deleteBooking response -> {}", "booking with id " + id + " not found");
        } catch (Exception e) {
            log.info("deleteBooking Exception -> {}", e.getMessage());
        }
    }

    @RabbitListener(queues = AUDIT_MESSAGE_QUEUE_NAME)
    @Override
    public void auditMessage(String message) {
        log.info("auditMessage request -> {}", message);
    }
}
