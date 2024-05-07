package com.example.bhopalmetroapp;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class JunctionDetails {
    private List<String> upcomingMetro;
    private List<String> nearParkingSlots;
    private String name;
    private String firstMetroTime;
    private List<String> facilities;
    private List<String> lines;
    private String lastMetroTime;

    // Constructor, getters, and setters
    public JunctionDetails() {
        // Default constructor required for Firebase
    }

    public List<String> getUpcomingMetro() {
        return upcomingMetro;
    }

    public void setUpcomingMetro(List<String> upcomingMetro) {
        this.upcomingMetro = upcomingMetro;
    }

    public List<String> getNearParkingSlots() {
        return nearParkingSlots;
    }

    public void setNearParkingSlots(List<String> nearParkingSlots) {
        this.nearParkingSlots = nearParkingSlots;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstMetroTime() {
        return firstMetroTime;
    }

    public void setFirstMetroTime(String firstMetroTime) {
        this.firstMetroTime = firstMetroTime;
    }

    public List<String> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<String> facilities) {
        this.facilities = facilities;
    }

    public List<String> getLines() {
        return lines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }

    public String getLastMetroTime() {
        return lastMetroTime;
    }

    public void setLastMetroTime(String lastMetroTime) {
        this.lastMetroTime = lastMetroTime;
    }
}