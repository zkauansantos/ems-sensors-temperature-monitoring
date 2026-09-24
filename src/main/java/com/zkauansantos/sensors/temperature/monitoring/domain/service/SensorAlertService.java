package com.zkauansantos.sensors.temperature.monitoring.domain.service;

import com.zkauansantos.sensors.temperature.monitoring.api.model.TemperatureLogData;
import com.zkauansantos.sensors.temperature.monitoring.domain.model.*;
import com.zkauansantos.sensors.temperature.monitoring.domain.repository.SensorAlertRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SensorAlertService {
    private final SensorAlertRepository sensorAlertRepository;

    @Transactional
    public void alert(TemperatureLogData logData) {
        sensorAlertRepository
                .findById(new SensorId(logData.getSensorId()))
                .ifPresentOrElse(
                        alert -> handleAlert(logData, alert),
                        () -> logIgnoredAlert(logData)
                );
    }

    public void handleAlert(TemperatureLogData logData, SensorAlert alert) {
        boolean shouldAlertMax = alert.getMaxTemperature() != null
                && logData.getValue().compareTo(alert.getMaxTemperature()) >= 0;

        if (shouldAlertMax) {
            log.info("Alert Max Temp: SensorId {} Temp {}", logData.getSensorId(), logData.getValue());
            return;
        }

        boolean shouldAlertMin = alert.getMinTemperature() != null
                && logData.getValue().compareTo(alert.getMinTemperature()) <= 0;

        if (shouldAlertMin) {
            log.info("Alert Min Temp: SensorId {} Temp {}", logData.getSensorId(), logData.getValue());
            return;
        }

        logIgnoredAlert(logData);
    }

    public void logIgnoredAlert(TemperatureLogData logData) {
        log.info("Alert Ignored: SensorId {} Temp {}", logData.getSensorId(), logData.getValue());
    }
}
