package com.message.producer.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties("producer.rabbitmq")
@Configuration
public class SystemProperty {
    private String messageExchangeName;
    private String bookingExchangeName;
    private String messageAuditQueueName;
    private String addBookingQueueName;
    private String editBookingQueueName;
    private String deleteBookingQueueName;
    private String addBookingQueueKey;
    private String editBookingQueueKey;
    private String deleteBookingQueueKey;
}
