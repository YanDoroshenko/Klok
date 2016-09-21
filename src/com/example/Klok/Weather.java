package com.example.Klok;

/**
 * Created by YanDoroshenko github.com/YanDoroshenko
 */
class Weather {

    private final WeatherCondition condition;
    private final boolean belowZero;
    private final String temperature, hi, lo, measurements;

    Weather(WeatherCondition condition, boolean belowZero, String temperature, String hi, String lo, String measurements) {
        this.condition = condition;
        this.belowZero = belowZero;
        this.temperature = temperature;
        this.hi = hi;
        this.lo = lo;
        this.measurements = measurements;
    }

    WeatherCondition getCondition() {
        return condition;
    }

    boolean isBelowZero() {
        return belowZero;
    }

    String getTemperature() {
        return temperature;
    }

    String getHi() {
        return hi;
    }

    String getLo() {
        return lo;
    }

    String getMeasurements() {
        return measurements;
    }
}
