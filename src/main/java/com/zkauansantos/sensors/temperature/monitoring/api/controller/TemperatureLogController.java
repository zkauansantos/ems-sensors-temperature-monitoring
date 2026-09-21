package com.zkauansantos.sensors.temperature.monitoring.api.controller;

import com.zkauansantos.sensors.temperature.monitoring.api.model.TemperatureLogOutput;
import com.zkauansantos.sensors.temperature.monitoring.domain.model.SensorId;
import com.zkauansantos.sensors.temperature.monitoring.domain.repository.TemperatureLogRepository;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sensors/{sensorId}/temperatures")
@RequiredArgsConstructor
public class TemperatureLogController {
    private final TemperatureLogRepository temperatureLogRepository;

    @GetMapping
    public Page<TemperatureLogOutput> search(@PathVariable TSID sensorId, @PageableDefault  Pageable pageable){
        return temperatureLogRepository.findAllBySensorId(new SensorId(sensorId), pageable)
                .map(temperature ->
                        TemperatureLogOutput.builder()
                        .id(temperature.getId().getValue())
                        .registeredAt(temperature.getRegisteredAt())
                        .sensorId(temperature.getSensorId().getValue())
                        .value(temperature.getValue())
                        .build()
                );
    }
}
