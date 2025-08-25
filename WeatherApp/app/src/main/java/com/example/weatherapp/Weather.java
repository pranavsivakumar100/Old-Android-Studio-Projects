package com.example.weatherapp;

public class Weather {
    private int image;
    private String time, temperature, description;

    public Weather(int image, String time, String temperature, String description) {
        this.image = image;
        this.time = time;
        this.temperature = temperature;
        this.description = description;
    }

    public int getImage() {
        return image;
    }

    public String getTime() {
        return time;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getDescription() {
        return description;
    }

}
