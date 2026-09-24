package com.zkauansantos.sensors.temperature.monitoring.infra.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.json.JsonMapper;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {
    private static final String PROCESS_TEMPERATURE_NAME = "temperature-monitoring.process-temperature.v1.";
    public static final String QUEUE_PROCESS_TEMPERATURE_NAME = PROCESS_TEMPERATURE_NAME + "q";
    public static final String DL_QUEUE_PROCESS_TEMPERATURE_NAME = PROCESS_TEMPERATURE_NAME + "dql";
    public static final String QUEUE_ALERT_TEMPERATURE_NAME = "temperature-monitoring.alert-temperature.v1.q";


    @Bean
    public JacksonJsonMessageConverter jacksonJsonMessageConverter(JsonMapper jsonMapper) {
        return new JacksonJsonMessageConverter(jsonMapper);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public Queue queueProcessTemperature(){
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", "");
        args.put("x-dead-letter-routing-key", DL_QUEUE_PROCESS_TEMPERATURE_NAME);

        return QueueBuilder.durable(QUEUE_PROCESS_TEMPERATURE_NAME).withArguments(args).build();
    }

    @Bean
    public Queue dlQueueProcessTemperature(){
        return QueueBuilder.durable(DL_QUEUE_PROCESS_TEMPERATURE_NAME).build();
    }

    @Bean
    public Queue queueAlertTemperature(){
        return QueueBuilder.durable(QUEUE_ALERT_TEMPERATURE_NAME).build();
    }

    @Bean
    public FanoutExchange exchange(){
        return ExchangeBuilder.fanoutExchange("temperature-processing.temperature-received.v1.e").build();
    }

    @Bean
    public Binding bindingProcessTemperature(){
        return BindingBuilder.bind(queueProcessTemperature()).to(exchange());
    }

    @Bean
    public Binding bindingAlertTemperature(){
        return BindingBuilder.bind(queueAlertTemperature()).to(exchange());
    }

}
