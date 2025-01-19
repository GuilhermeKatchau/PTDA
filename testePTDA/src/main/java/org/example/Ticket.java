package org.example;

import java.util.ArrayList;

public class Ticket {

    private int id_Ticket; // identificador unico do ticket
    private String destination; // destino do voo
    private String source; // origem do voo
    private double price; // preco do ticket
    private Passenger passenger; // passageiro associado ao ticket
    private Class flightClass; // classe do voo (ex: economica, executiva)
    private Flight flight; // voo associado ao ticket
    ArrayList<Ticket> tickets = new ArrayList<Ticket>(); // lista de tickets

    // construtor da classe ticket
    public Ticket(String destination, String source, int id_Ticket, double price) {
        setId_Ticket(id_Ticket); // define o id do ticket
        setSource(source); // define a origem do voo
        setDestination(destination); // define o destino do voo
        setPrice(price); // define o preco do ticket
    }

    // retorna a classe do voo associada ao ticket
    public Class getFlightClass() {
        return flightClass;
    }

    // retorna o passageiro associado ao ticket
    public Passenger getPassenger() {
        return passenger;
    }

    // define o id do ticket, validando se esta dentro do intervalo permitido
    public void setId_Ticket(int id_Ticket) {
        if (id_Ticket >= 1 && id_Ticket <= 999999999) {
            this.id_Ticket = id_Ticket;
        } else {
            System.out.println("ID de Ticket invalido");
        }
    }

    // retorna o id do ticket
    public int getId_Ticket() {
        return id_Ticket;
    }

    // define a origem do voo, validando se nao esta vazia e se e diferente do destino
    public void setSource(String source) {
        if (source != null && !source.trim().isEmpty() && !source.equals(destination)) {
            this.source = source;
        } else {
            throw new IllegalArgumentException("Origem de Voo nao pode ser vazia");
        }
    }

    // retorna a origem do voo
    public String getSource() {
        return source;
    }

    // define o destino do voo, validando se nao esta vazio e se e diferente da origem
    public void setDestination(String destination) {
        if (destination != null && !destination.trim().isEmpty() && !destination.equals(source)) {
            this.destination = destination;
        } else {
            throw new IllegalArgumentException("Destino de Voo nao pode ser vazio");
        }
    }

    // retorna o destino do voo
    public String getDestination() {
        return destination;
    }

    // define o preco do ticket, validando se esta dentro do intervalo permitido
    public void setPrice(double price) {
        if (price > 100 && price <= 1000000) {
            this.price = price;
        } else {
            throw new IllegalArgumentException("Preco invalido");
        }
    }

    // retorna uma representacao em string do ticket
    @Override
    public String toString() {
        return "Ticket{" +
                "id_Ticket=" + id_Ticket +
                ", destination='" + destination + '\'' +
                ", source='" + source + '\'' +
                ", price=" + price;
    }

    // retorna o preco do ticket
    public double getPrice() {
        return price;
    }

    // define o voo associado ao ticket
    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    // retorna o voo associado ao ticket
    public Flight getFlight() {
        return flight;
    }

    // exibe os servicos disponiveis para o passageiro, baseado na classe do voo
    public void displayPassengerServices() {
        System.out.println("Passageiro: " + passenger.getName());
        System.out.println("Id: " + passenger.getId_Passenger());
        System.out.println("Classe: " + flightClass.getClassName());
        System.out.println("Servicos disponiveis: ");
        for (String service : flightClass.getServices()) {
            System.out.println("- " + service);
        }
        System.out.println();
    }
}