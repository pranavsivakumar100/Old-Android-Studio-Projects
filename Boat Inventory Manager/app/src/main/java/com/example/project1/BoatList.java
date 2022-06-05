package com.example.project1;

import java.util.ArrayList;

public class BoatList {
    private static BoatList instance = null;
    private ArrayList<Boat> boats;
    private BoatList() {
        boats = new ArrayList<>();
    }
    public static BoatList getInstance() {
        if (instance == null) {
            instance = new BoatList();
        }
        return instance;
    }

    public void addBoat(Boat boat) {
        boats.add(boat);
    }

    public int size() {
        return boats.size();
    }

    public Boat getBoat(int index) {
        if (index >= 0 && index < boats.size()) {
            return boats.get(index);
        }
        return null;
    }

    public Boat getBoatByRegistration(String registrationNumber) {
        for (Boat boat : boats) {
            if (boat.getRegistrationNumber().equals(registrationNumber)) {
                return boat;
            }
        }
        return null;
    }
    public boolean removeBoatByRegistration(String registrationNumber) {
        for (int i = 0; i < boats.size(); i++) {
            if (boats.get(i).getRegistrationNumber().equals(registrationNumber)) {
                boats.remove(i);
                return true;
            }
        }
        return false;
    }
    public void clear() {
        boats.clear();
    }
    public double getAveragePrice() {
        if (boats.size() == 0) {
            return 0.0;
        }

        double totalPrice = 0.0;
        for (Boat boat : boats) {
            totalPrice += boat.getPrice();
        }

        return totalPrice / boats.size();
    }

    public int getDieselBoatCount() {
        int count = 0;
        for (Boat boat : boats) {
            if (boat.getFuelType().equalsIgnoreCase("diesel")) {
                count++;
            }
        }
        return count;
    }
    public String getAllBoatsAsString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < boats.size(); i++) {
            sb.append(boats.get(i).toString());
            if (i < boats.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}