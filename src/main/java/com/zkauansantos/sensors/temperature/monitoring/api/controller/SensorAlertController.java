package com.zkauansantos.sensors.temperature.monitoring.api.controller;

import com.zkauansantos.sensors.temperature.monitoring.api.model.SensorAlertInput;
import com.zkauansantos.sensors.temperature.monitoring.api.model.SensorAlertOutput;
import com.zkauansantos.sensors.temperature.monitoring.domain.model.SensorAlert;
import com.zkauansantos.sensors.temperature.monitoring.domain.model.SensorId;
import com.zkauansantos.sensors.temperature.monitoring.domain.repository.SensorAlertRepository;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RequestMapping("/api/sensors/{sensorId}/alert")
@RestController
@RequiredArgsConstructor
public class SensorAlertController {
    private final SensorAlertRepository alertRepository;

    @GetMapping
    public SensorAlertOutput getOne(@PathVariable TSID sensorId) {
        return alertRepository.findById(new SensorId(sensorId))
                .map(alert -> SensorAlertOutput.builder()
                        .id(alert.getId().getValue())
                        .maxTemperature(alert.getMaxTemperature())
                        .minTemperature(alert.getMinTemperature())
                        .build()
                )
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable TSID sensorId, @RequestBody SensorAlertInput input) {
        SensorId id = new SensorId(sensorId);

        SensorAlert alert = alertRepository.findById(id)
                .orElseGet(() -> SensorAlert.builder()
                        .id(id)
                        .build());

        alert.setMaxTemperature(input.getMaxTemperature());
        alert.setMinTemperature(input.getMinTemperature());

        alertRepository.save(alert);
    }

    @DeleteMapping
    public void delete(@PathVariable TSID sensorId) {
         SensorAlert alert = alertRepository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

         alertRepository.delete(alert);
    }
}
