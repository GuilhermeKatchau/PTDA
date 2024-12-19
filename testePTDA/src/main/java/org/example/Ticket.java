package org.example;

import java.util.ArrayList;

public class Ticket {

    private int id_Ticket;
    private String destination;
    private String source;
    private double price;
    private Passenger passenger;
    private Class flightClass;
    private Flight flight;
    ArrayList<Ticket> tickets = new ArrayList<Ticket>();


    public Ticket(String destination, String source, int id_Ticket, double price) {
        setId_Ticket(id_Ticket);
        setSource(source);
        setDestination(destination);
        setPrice(price);
    }


    public Class getFlightClass() {
        return flightClass;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setId_Ticket(int id_Ticket) {
        if (id_Ticket >= 1 && id_Ticket <= 999999999) {
            this.id_Ticket = id_Ticket;
        } else {
            System.out.println("ID de Ticket inválido");
        }
    }

    public int getId_Ticket() {
        return id_Ticket;
    }

    public void setSource(String source) {
        if (source != null && !source.trim().isEmpty()) {
            this.source = source;
        } else {
            throw new IllegalArgumentException("Origem de Voo não pode ser vazia");
        }
    }

    public String getSource() {
        return source;
    }

    public void setDestination(String destination) {
        if (destination != null && !destination.trim().isEmpty()) {
            this.destination = destination;
        } else {
            throw new IllegalArgumentException("Destino de Voo não pode ser vazio");
        }
    }

    public String getDestination() {
        return destination;
    }

    public void setPrice(double price) {
        if (price > 100 && price <= 1000000) {
            this.price = price;
        } else {
            throw new IllegalArgumentException("Preço inválido");
        }
    }

    public double getPrice() {
        return price;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public Flight getFlight() {
        return flight;
    }

    public void displayPassengerServices() {
        System.out.println("Passageiro: " + passenger.getName());
        System.out.println("Id: " + passenger.getId_Passenger());
        System.out.println("Classe: " + flightClass.getClassName());
        System.out.println("Serviços disponíveis: ");
         for (Service service : flightClass.getServices()) {
             System.out.println("- " + service.getName());
         }
        System.out.println();
    }

}
