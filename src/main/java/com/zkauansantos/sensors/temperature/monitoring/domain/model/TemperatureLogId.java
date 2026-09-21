package com.zkauansantos.sensors.temperature.monitoring.domain.model;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TemperatureLogId {
    private UUID value;

    public TemperatureLogId(UUID value){
        Objects.requireNonNull(value);
        this.value = value;
    }

    public TemperatureLogId(String value){
        Objects.requireNonNull(value);
        this.value = UUID.fromString(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
