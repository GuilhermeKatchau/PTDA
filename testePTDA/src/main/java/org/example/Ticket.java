package org.example;

public class Ticket {
    private int id_Ticket;
    private String destination;
    private String source;
    private double price;
    private String namePassenger;
    private int id_Passenger;
    private int id_Seat;
    private int idFlight;

    public Ticket(String destination, String source, double price, String namePassenger, int id_Passenger, int id_Seat, int idFlight) {
        this.destination = destination;
        this.source = source;
        this.id_Ticket = id_Ticket;
        this.price = price;
        this.namePassenger = namePassenger;
        this.id_Passenger = id_Passenger;
        this.id_Seat = id_Seat;
        this.idFlight = idFlight;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id_Ticket=" + id_Ticket +
                ", destination='" + destination + '\'' +
                ", source='" + source + '\'' +
                ", price=" + price +
                ", namePassenger='" + namePassenger + '\'' +
                ", id_Passenger=" + id_Passenger +
                ", id_Seat=" + id_Seat +
                ", idFlight=" + idFlight +
                '}';
    }
}
