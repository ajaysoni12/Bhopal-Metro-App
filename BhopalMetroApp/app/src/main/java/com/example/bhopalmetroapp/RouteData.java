package com.example.bhopalmetroapp;

import java.util.List;
import java.util.Map;

class Station {
    private String name;
    private int code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    // Constructor, getters, and setters
}

class TrainTimes {
    private List<String> arrival;
    private List<String> departure;

    public List<String> getArrival() {
        return arrival;
    }

    public void setArrival(List<String> arrival) {
        this.arrival = arrival;
    }

    public List<String> getDeparture() {
        return departure;
    }

    public void setDeparture(List<String> departure) {
        this.departure = departure;
    }
}

class Line {
    private List<Station> stations;
    private Map<Integer, TrainTimes> trainTimes;

    public List<Station> getStations() {
        return stations;
    }

    public void setStations(List<Station> stations) {
        this.stations = stations;
    }

    public Map<Integer, TrainTimes> getTrainTimes() {
        return trainTimes;
    }

    public void setTrainTimes(Map<Integer, TrainTimes> trainTimes) {
        this.trainTimes = trainTimes;
    }
}

public class RouteData {
    private Map<String, Line> routes;

    public Map<String, Line> getRoutes() {
        return routes;
    }

    public void setRoutes(Map<String, Line> routes) {
        this.routes = routes;
    }
}
