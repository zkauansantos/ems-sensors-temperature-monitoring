package com.zkauansantos.sensors.temperature.monitoring.infra.rabbitmq;

import com.zkauansantos.sensors.temperature.monitoring.api.model.TemperatureLogData;
import com.zkauansantos.sensors.temperature.monitoring.domain.service.SensorAlertService;
import com.zkauansantos.sensors.temperature.monitoring.domain.service.TemperatureMonitoringService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import java.util.Map;

import static com.zkauansantos.sensors.temperature.monitoring.infra.rabbitmq.RabbitMQConfig.QUEUE_ALERT_TEMPERATURE_NAME;
import static com.zkauansantos.sensors.temperature.monitoring.infra.rabbitmq.RabbitMQConfig.QUEUE_PROCESS_TEMPERATURE_NAME;


@Component
@Slf4j
@RequiredArgsConstructor
public class RabbitMQListener {
    private final TemperatureMonitoringService temperatureMonitoringService;
    private final SensorAlertService sensorAlertService;

    @RabbitListener(queues = QUEUE_PROCESS_TEMPERATURE_NAME, concurrency = "2-3")
    @SneakyThrows
    public void handleProcessingTemperature(
            @Payload TemperatureLogData logData,
            @Headers Map<String, Object> headers
    )  {
        temperatureMonitoringService.processTemperature(logData);
    }

    @RabbitListener(queues = QUEUE_ALERT_TEMPERATURE_NAME, concurrency = "2-3")
    @SneakyThrows
    public void handleAlerting(
            @Payload TemperatureLogData logData,
            @Headers Map<String, Object> headers
    )  {
        sensorAlertService.alert(logData);
    }
}
