package com.example.project1;
public class Boat {

    public static final int LENGTH_THRESHOLD = 50;
    public static final int AGE_THRESHOLD = 10;
    public static final double LUXURY_TAX_RATE = 0.15;

    private String make;
    private String registrationNumber;
    private int year;
    private int length;
    private int beam;
    private String fuelType;
    private double price;

    public Boat(String make, String registrationNumber, int year, int length, int beam, String fuelType, double price) {
        this.make = make;
        this.registrationNumber = registrationNumber;
        this.year = year;
        this.length = length;
        this.beam = beam;
        this.fuelType = fuelType;
        this.price = price;
    }

    public Boat() {
        this.make = "";
        this.registrationNumber = "";
        this.year = 0;
        this.length = 0;
        this.beam = 0;
        this.fuelType = "";
        this.price = 0.0;
    }
    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getBeam() {
        return beam;
    }

    public void setBeam(int beam) {
        this.beam = beam;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAge() {
        return DateUtil.get_current_year() - year;
    }

    public double getLuxuryTax() {
        if (length >= LENGTH_THRESHOLD && getAge() <= AGE_THRESHOLD) {
            return price * LUXURY_TAX_RATE;
        }
        return 0.0;
    }

    public String getFormattedBeam() {
        int feet = beam / 12;
        int inches = beam % 12;
        return feet + " ft, " + inches + " in";
    }

    @Override
    public String toString() {
        return make + ", " + year + ", " + registrationNumber;
    }
}