package com.zkauansantos.sensors.temperature.monitoring.api.model;

import io.hypersistence.tsid.TSID;
import lombok.Data;

@Data
public class SensorAlertInput {
    private TSID id;
    private Double maxTemperature;
    private Double minTemperature;
}
