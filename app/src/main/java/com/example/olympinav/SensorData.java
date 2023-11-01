package com.example.olympinav;

public enum SensorData {
    NoiseBaseLevelThreshold(10), // magic processed sensor data
    PrioritiseSeatsThreshold(25); // magic processed sensor data

    private final int value;

    SensorData(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
