package com.zkauansantos.sensors.temperature.monitoring.domain.service;

import com.zkauansantos.sensors.temperature.monitoring.api.model.TemperatureLogData;
import com.zkauansantos.sensors.temperature.monitoring.domain.model.SensorId;
import com.zkauansantos.sensors.temperature.monitoring.domain.model.SensorMonitoring;
import com.zkauansantos.sensors.temperature.monitoring.domain.model.TemperatureLog;
import com.zkauansantos.sensors.temperature.monitoring.domain.model.TemperatureLogId;
import com.zkauansantos.sensors.temperature.monitoring.domain.repository.SensorMonitoringRepository;
import com.zkauansantos.sensors.temperature.monitoring.domain.repository.TemperatureLogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemperatureMonitoringService {
    private final SensorMonitoringRepository sensorMonitoringRepository;
    private final TemperatureLogRepository temperatureLogRepository;

    @Transactional
    public void processTemperature(TemperatureLogData logData) {
        sensorMonitoringRepository
                .findById(new SensorId(logData.getSensorId()))
                .ifPresentOrElse(
    sensor -> handleSensorMonitoring(logData, sensor),
                    () -> logIgnoredTemperature(logData)
                );
    }

    public void handleSensorMonitoring(TemperatureLogData logData, SensorMonitoring sensorMonitoring) {
        if(sensorMonitoring.isEnabled()){
            sensorMonitoring.setLastTemperature(logData.getValue());
            sensorMonitoring.setUpdatedAt(OffsetDateTime.now());
            sensorMonitoringRepository.saveAndFlush(sensorMonitoring);

            TemperatureLog temperature = TemperatureLog.builder()
                    .id(new TemperatureLogId(logData.getId()))
                    .registeredAt(logData.getRegisteredAt())
                    .value(logData.getValue())
                    .sensorId(new SensorId(logData.getSensorId()))
                    .build();

            temperatureLogRepository.saveAndFlush(temperature);
            return;
        }

        logIgnoredTemperature(logData);
    }

    public void logIgnoredTemperature(TemperatureLogData logData) {
        log.info("Temperature Ignored: SensorId {} Temp {}", logData.getSensorId(), logData.getValue());
    }
}
