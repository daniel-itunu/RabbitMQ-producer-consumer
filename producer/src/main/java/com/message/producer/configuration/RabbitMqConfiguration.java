package com.message.producer.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.amqp.core.BindingBuilder.bind;

@Configuration
public class RabbitMqConfiguration {
    private final SystemProperty systemProperty;

    public RabbitMqConfiguration(SystemProperty systemProperty) {
        this.systemProperty = systemProperty;
    }


    @Bean
    Queue messageAuditQueue() {
        return new Queue(systemProperty.getMessageAuditQueueName(), true);
    }

    @Bean
    Queue addBookingQueue() {
        return new Queue(systemProperty.getAddBookingQueueName(), true);
    }

    @Bean
    Queue editBookingQueue() {
        return new Queue(systemProperty.getEditBookingQueueName(), true);
    }

    @Bean
    Queue deleteBookingQueue() {
        return new Queue(systemProperty.getDeleteBookingQueueName(), true);
    }

    @Bean
    FanoutExchange messageExchange() {
        return ExchangeBuilder.fanoutExchange(systemProperty.getMessageExchangeName()).durable(true).build();
    }

    @Bean
    DirectExchange bookingExchange() {
        return ExchangeBuilder.directExchange(systemProperty.getBookingExchangeName()).durable(true).build();
    }

    @Bean
    Binding addBookingQueueToBookingExchangeBinding() {
       return bind(addBookingQueue())
                .to(bookingExchange())
                .with(systemProperty.getAddBookingQueueKey());
    }

    @Bean
    Binding editBookingQueueToBookingExchangeBinding() {
         return bind(editBookingQueue())
                .to(bookingExchange())
                .with(systemProperty.getEditBookingQueueKey());
    }

    @Bean
    Binding deleteBookingQueueToBookingExchangeBinding() {
        return bind(deleteBookingQueue())
                .to(bookingExchange())
                .with(systemProperty.getDeleteBookingQueueKey());
    }

    @Bean
    Binding messageAuditQueueToMessagesEntryPointExchangeBinding() {
        return bind(messageAuditQueue())
                .to(messageExchange());
    }

    @Bean
    Binding bookingExchangeToMessagesEntryPointExchangeBinding() {
        return bind(bookingExchange())
                .to(messageExchange());
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setExchange(systemProperty.getMessageExchangeName());
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
