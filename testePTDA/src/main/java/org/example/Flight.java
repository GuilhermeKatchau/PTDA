package org.example;

import javax.swing.*;
import java.util.*;

import static java.lang.String.valueOf;

public class Flight {

    private int id_Flight;
    private static int id_Airplane;
    private String codeName;
    private String source;
    private String destination;
    private int maxPassengers;
    private int nCompany;
    private Date date1;
    private Date hTakeOff;
    private Date hLanding;

    private static final ArrayList<Flight> flights = new ArrayList<>();

    public Flight(int id_Airplane, int id_Flight, int maxPassengers, Date date1, Date hTakeOff, Date hLanding, String destination, String source, String codename) {
        setId_Airplane(id_Airplane);
        setId_Flight(id_Flight);
        setCodeName(codename);
        setSource(source);
        setDestination(destination);
        setMaxPassengers(maxPassengers);
        setDate1(date1);
        setHTakeoff(hTakeOff);
        setHLanding(hLanding);
    }
    private void setId_Airplane(int id_Airplane) {
        if (id_Airplane >= 1 && id_Airplane <= 999999999) {
            this.id_Airplane = id_Airplane;
        } else {
            throw new IllegalArgumentException("ID de Avião inválido");
        }
    }

    public int getId_Airplane() {
        return id_Airplane;
    }

    private void setDate1(Date date1) {
        if (date1 != null) {
            this.date1 = date1;
        } else {
            throw new IllegalArgumentException("Data de Voo inválida");
        }
    }
    public Date getDate1() {
        return date1;
    }


    // Getters e Setters
    public void setMaxPassengers(int maxPassengers) {
        if (maxPassengers > 0 && maxPassengers <= 900) {
            this.maxPassengers = maxPassengers;
        } else {
            throw new IllegalArgumentException("Número de passageiros inválido");
        }
    }

    public int getMaxPassengers() {
        return maxPassengers;
    }

    public void setId_Flight(int id_Flight) {
        if (id_Flight >=1 || id_Flight <= 999999999) {
            this.id_Flight = id_Flight;
        } else {
            throw new IllegalArgumentException("ID de Voo inválido");
        }
    }

    public int getId_Flight() {
        return id_Flight;
    }

    public void setHTakeoff(Date hTakeoff) {
        this.hTakeOff = hTakeoff;
    }

    public Date gethTakeoff() {
        return hTakeOff;
    }

    public void setHLanding(Date hLanding) {
        this.hLanding = hLanding;
    }

    public Date gethLanding() {
        return hLanding;
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

    public void setCodeName(String codeName) {
        if (codeName != null && !codeName.trim().isEmpty()) {
            this.codeName = codeName;
        } else {
            throw new IllegalArgumentException("Nome de Voo não pode ser vazio");
        }
    }

    public String getCodeName() {
        return codeName;
    }





    public static void addFlight(int id_Flight, int maxPassengers,Date date1, Date hTakeOff, Date hLanding, String destination, String source, String codename) {

            if (id_Flight==0 || codename.isEmpty()

                    || source.isEmpty() || destination.isEmpty()) {
                throw new IllegalArgumentException("Por favor, preencha todos os campos!");

            }
            if(String.valueOf(date1).isEmpty()){
                throw new IllegalArgumentException("Por favor, preencha todos os campos!");
            }
            if (!hTakeOff.before(hLanding) || (hLanding.getTime() - hTakeOff.getTime()) < 60000) {
                throw new IllegalArgumentException("A data e hora de partida devem ser ANTES da data e hora de chegada, com pelo menos 1 minuto de diferença.");
            }

            Flight newFlight = new Flight(id_Airplane,id_Flight, maxPassengers, date1, hTakeOff, hLanding, destination, source, codename);
            flights.add(newFlight);
            JOptionPane.showMessageDialog(null, "Voo adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void removeFlight(int flightIndex) {
        if (flightIndex >= 0 && flightIndex < flights.size()) {
            flights.remove(flightIndex);
            JOptionPane.showMessageDialog(null, "Voo removido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Índice de voo inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static ArrayList<Flight> getFlights() {
        return new ArrayList<>(flights);
    }

    public static void removeTestFlights() {
        flights.removeIf(flight -> flight.getCodeName().startsWith("TEST_"));
    }

    @Override
    public String toString() {
        return "ID Avião: " + id_Airplane +
                " | ID Voo: " + id_Flight +
                " | Code Name: " + codeName +
                " | Origem: " + source +
                " | Destino: " + destination +
                " | Data: " + date1 +
                " | Partida: " + hTakeOff +
                " | Chegada: " + hLanding +
                " | Limite: " + maxPassengers;
    }
}
