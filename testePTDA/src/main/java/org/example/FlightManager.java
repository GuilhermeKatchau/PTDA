package org.example;

import java.util.ArrayList;

public class FlightManager {
    private static FlightManager instance;
    private final ArrayList<Flight> flights;

    private FlightManager() {
        flights = new ArrayList<>();
    }

    public static FlightManager getInstance() {
        if (instance == null) {
            instance = new FlightManager();
        }
        return instance;
    }

    public void addFlight(Flight flight) {
        flights.add(flight);
    }

    public ArrayList<Flight> getFlights() {
        return flights;
    }
}
