package com.message.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.message.producer.dto.BookingDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.RabbitMQContainer;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ProducerApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@PropertySource("classpath:application-test.properties")
class ProducerApplicationTests {
	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	MockMvc mockMvc;
	static RabbitMQContainer rabbit;


	@BeforeAll
	static void startRabbit(){
		rabbit = new RabbitMQContainer("rabbitmq:3.7.25-management-alpine");
		rabbit.start();
	}
	@AfterAll
	static void stopRabbit(){
		rabbit.stop();
	}

	@DynamicPropertySource
	public static void rabbitProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.rabbitmq.port", rabbit::getFirstMappedPort);
	}

	@Test
	void editBookingShouldBeSuccessful() throws Exception {
		BookingDto bookingDto = new BookingDto();
		bookingDto.setBookingId("111");
		bookingDto.setPassengerName("name");
		bookingDto.setContactNumber("number");
		mockMvc.perform(MockMvcRequestBuilders.put("/booking")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(bookingDto)))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
				.andExpect(jsonPath("$.status", Matchers.is("Success")))
				.andExpect(jsonPath("$.statusCode", Matchers.is("1")))
				.andExpect(jsonPath("$.statusMessage", Matchers.is("edit-booking published successfully")));
	}

	@Test
	void addBookingShouldBeSuccessful() throws Exception {
		BookingDto bookingDto = new BookingDto();
		bookingDto.setBookingId("id");
		bookingDto.setPassengerName("name");
		mockMvc.perform(MockMvcRequestBuilders.post("/booking")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(bookingDto)))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
				.andExpect(jsonPath("$.status", Matchers.is("Success")))
				.andExpect(jsonPath("$.statusCode", Matchers.is("1")))
				.andExpect(jsonPath("$.statusMessage", Matchers.is("add-booking published successfully")));
	}

	@Test
	void addBookingShouldThrowMethodArgumentException() throws Exception {
		BookingDto bookingDto = new BookingDto();
		mockMvc.perform(MockMvcRequestBuilders.post("/booking")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(bookingDto)))
				.andExpect(status().isBadRequest())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
				.andExpect(jsonPath("$.status", Matchers.is("Failed")))
				.andExpect(jsonPath("$.statusCode", Matchers.is("0")))
				.andExpect(jsonPath("$.statusMessage", Matchers.is("Invalid field provided")));
	}

	@Test
	void deleteBookingShouldBeSuccessful() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/booking/{bookingId}", "bookingId"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
				.andExpect(jsonPath("$.status", Matchers.is("Success")))
				.andExpect(jsonPath("$.statusCode", Matchers.is("1")))
				.andExpect(jsonPath("$.statusMessage", Matchers.is("delete-booking published successfully")));
	}

	@Test
	void deleteBookingShouldBeBadRequest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/booking/{bookingId}", " "))
				.andExpect(status().isBadRequest())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
				.andExpect(jsonPath("$.status", Matchers.is("Failed")))
				.andExpect(jsonPath("$.statusCode", Matchers.is("0")))
				.andExpect(jsonPath("$.statusMessage", Matchers.is("Booking id is required")));
	}

	@Test
	void contextLoads() {
	}

}
