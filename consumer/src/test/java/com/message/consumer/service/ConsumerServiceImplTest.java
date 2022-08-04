package com.message.consumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.message.consumer.model.Booking;
import com.message.consumer.model.TripWayPoint;
import com.message.consumer.repository.BookingRepository;
import com.message.consumer.repository.TripWayPointRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@SpringBootTest
class ConsumerServiceImplTest {
    @Autowired
    ConsumerService consumerService;
    @MockBean
    BookingRepository bookingRepository;
    @MockBean
    TripWayPointRepository tripWayPointRepository;
    private static String bookingMessage ="{\n" +
            "    \"bookingId\": \"bookingId\",\n" +
            "    \"passengerName\": \"name\",\n" +
            "    \"contactNumber\": \"contact\",\n" +
            "    \"pickupTime\": \"pickup\",\n" +
            "    \"asap\": \"asap\",\n" +
            "    \"waitingTime\": \"waitingTime\",\n" +
            "    \"numberOfPassengers\": \"numberOfPassengers\",\n" +
            "    \"price\": \"price\",\n" +
            "    \"rating\": \"rating\",\n" +
            "    \"tripWayPoints\": [\n" +
            "        {\n" +
            "            \"tripWayPointId\": \"1\",\n" +
            "            \"locality\": \"locality\",\n" +
            "            \"longitude\": \"longitude\",\n" +
            "            \"latitude\": \"latitude\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"tripWayPointId\": \"2\",\n" +
            "            \"locality\": \"locality\",\n" +
            "            \"longitude\": \"longitude\",\n" +
            "            \"latitude\": \"latitude\"\n" +
            "        }\n" +
            "    ]\n" +
            "}";
    private static Booking booking;
    @BeforeAll
    static void beforeAll() throws JsonProcessingException {
        booking = new ObjectMapper().readValue(bookingMessage, Booking.class);
    }

    @Test
    void addBookingShouldBeSuccessful() {
        try {
            Booking booking = new ObjectMapper().readValue(bookingMessage, Booking.class);
            Mockito.when(bookingRepository.save(booking)).thenReturn(booking);
            consumerService.addBooking(bookingMessage);
            assertTrue(true);
        }catch (Exception e){
            log.error("addBookingShouldBeSuccessful Test failure -> {}", e.getMessage());
            assertTrue(false);
        }
    }

    @Test
    void addBookingShouldCatchException() {
        String malformedMessage = "-malformed:message[";
        try {
            assertDoesNotThrow(() -> consumerService.addBooking(malformedMessage));
            assertTrue(true);
        } catch (Exception e){
            log.error("addBookingShouldCatchException Test failure -> {}", e.getMessage());
            assertTrue(false);
        }
    }

    @Test
    void editBookingShouldBeSuccessful() {
        try {

            TripWayPoint tripWayPoint = booking.getTripWayPoints().get(0);
            Mockito.when(bookingRepository.findBookingByBookingId(booking.getBookingId())).thenReturn(booking);
            Mockito.when(tripWayPointRepository.findTripWayPointByTripWayPointId(tripWayPoint.getTripWayPointId())).thenReturn(tripWayPoint);
            Mockito.when(bookingRepository.save(booking)).thenReturn(booking);
            consumerService.editBooking(bookingMessage);
            assertTrue(true);
        }catch (Exception e){
            log.error("editBookingShouldBeSuccessful Test failure -> {}", e.getMessage());
            assertTrue(false);
        }
    }

    @Test
    void editBookingBookingIdShouldBeNull() {
        try {
           String message = "{\n" +
                   "    \"bookingId\": null,\n" +
                   "    \"passengerName\": \"name\"\n" +
                   "    }";
            consumerService.editBooking(message);
            assertTrue(true);
        }catch (Exception e){
            log.error("editBookingBookingIdShouldBeNull Test failure -> {}", e.getMessage());
            assertTrue(false);
        }
    }

    @Test
    void editBookingBookingIdShouldBeNotFound() {
        try {
            Mockito.when(bookingRepository.findBookingByBookingId("booking")).thenReturn(null);
            consumerService.editBooking(bookingMessage);
            assertTrue(true);
        }catch (Exception e){
            log.error("editBookingBookingIdShouldBeNotFound Test failure -> {}", e.getMessage());
            assertTrue(false);
        }
    }

    @Test
    void editBookingShouldCatchException() {
        String malformedMessage = "-malformed:message[";
        try {
            assertDoesNotThrow(() -> consumerService.editBooking(malformedMessage));
            assertTrue(true);
        } catch (Exception e){
            log.error("editBookingShouldCatchException Test failure -> {}", e.getMessage());
            assertTrue(false);
        }
    }

    @Test
    void deleteBookingShouldBeSuccessful() {
        try {
            Mockito.when(bookingRepository.findBookingByBookingId(booking.getBookingId())).thenReturn(booking);
            doNothing().doThrow( new IllegalStateException()).when(bookingRepository).delete(booking);
            consumerService.deleteBooking("\""+booking.getBookingId()+"\"");
            assertTrue(true);
        }catch (Exception e){
            log.error("deleteBookingShouldBeSuccessful Test failure -> {}", e.getMessage());
            assertTrue(false);
        }
    }

    @Test
    void deleteBookingBookingIdShouldBeNotFound() {
        try {
            Mockito.when(bookingRepository.findBookingByBookingId(booking.getBookingId())).thenReturn(booking);
            doNothing().doThrow(new IllegalStateException()).when(bookingRepository).delete(booking);
            booking.setBookingId(null);
            consumerService.deleteBooking("\""+booking.getBookingId()+"\"");
            assertTrue(true);
        }catch (Exception e){
            log.error("deleteBookingBookingIdShouldBeNotFound Test failure -> {}", e.getMessage());
            assertTrue(false);
        }
    }

    @Test
    void deleteBookingShouldCatchException() {
        String malformedMessage = "bookingId";
        try {
            assertDoesNotThrow(() -> consumerService.deleteBooking(malformedMessage));
            assertTrue(true);
        } catch (Exception e){
            log.error("deleteBookingShouldCatchException Test failure -> {}", e.getMessage());
            assertTrue(false);
        }
    }

    @Test
    void auditMessage() {
        try {
            consumerService.auditMessage("audit message");
            assertTrue(true);
        } catch (Exception e) {
            log.error("auditMessage Test failure -> {}", e.getMessage());
            assertTrue(false);
        }
    }
}