package org.example;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;

public class Flight {

    private int id_Airplane;
    private int id_Flight;
    private String codeName;
    private String source;
    private String destination;
    private int maxPassengers;
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

    // Getters and Setters

    public int getId_Airplane() {
        return id_Airplane;
    }

    public void setId_Airplane(int id_Airplane) {
        if (id_Airplane >= 1 && id_Airplane <= 999999999) {
            this.id_Airplane = id_Airplane;
        } else {
            throw new IllegalArgumentException("ID de Avião inválido");
        }
    }

    public int getId_Flight() {
        return id_Flight;
    }

    public void setId_Flight(int id_Flight) {
        if (id_Flight >= 1 && id_Flight <= 999999999) {
            this.id_Flight = id_Flight;
        } else {
            throw new IllegalArgumentException("ID de Voo inválido");
        }
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        if (codeName != null && !codeName.trim().isEmpty()) {
            this.codeName = codeName;
        } else {
            throw new IllegalArgumentException("Código do Voo não pode ser vazio");
        }
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        if (source != null && !source.trim().isEmpty()) {
            this.source = source;
        } else {
            throw new IllegalArgumentException("Origem não pode ser vazia");
        }
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        if (destination != null && !destination.trim().isEmpty()) {
            this.destination = destination;
        } else {
            throw new IllegalArgumentException("Destino não pode ser vazio");
        }
    }

    public int getMaxPassengers() {
        return maxPassengers;
    }

    public void setMaxPassengers(int maxPassengers) {
        if (maxPassengers > 0 && maxPassengers <= 900) {
            this.maxPassengers = maxPassengers;
        } else {
            throw new IllegalArgumentException("Número de passageiros inválido");
        }
    }

    public Date getDate1() {
        return date1;
    }

    public void setDate1(Date date1) {
        if (date1 != null) {
            this.date1 = date1;
        } else {
            throw new IllegalArgumentException("Data inválida");
        }
    }

    public Date getHTakeoff() {
        return hTakeOff;
    }

    public void setHTakeoff(Date hTakeoff) {
        if (hTakeoff != null) {
            this.hTakeOff = hTakeoff;
        } else {
            throw new IllegalArgumentException("Horário de partida inválido");
        }
    }

    public Date getHLanding() {
        return hLanding;
    }

    public void setHLanding(Date hLanding) {
        if (hLanding != null) {
            this.hLanding = hLanding;
        } else {
            throw new IllegalArgumentException("Horário de chegada inválido");
        }
    }

    public static void addFlight(int id_Airplane, int id_Flight, int maxPassengers, Date date1, Date hTakeOff, Date hLanding, String destination, String source, String codename) {
        if (id_Flight <= 0 || codename.isEmpty() || source.isEmpty() || destination.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, preencha todos os campos corretamente.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!hTakeOff.before(hLanding) || (hLanding.getTime() - hTakeOff.getTime()) < 60000) {
            JOptionPane.showMessageDialog(null, "A data e hora de partida devem ser ANTES da data e hora de chegada, com pelo menos 1 minuto de diferença.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Flight newFlight = new Flight(id_Airplane, id_Flight, maxPassengers, date1, hTakeOff, hLanding, destination, source, codename);
            flights.add(newFlight);
            JOptionPane.showMessageDialog(null, "Voo adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static ArrayList<Flight> getFlights() {
        return new ArrayList<>(flights);
    }

    @Override
    public String toString() {
        return "ID Avião: " + id_Airplane +
                " | ID Voo: " + id_Flight +
                " | Código: " + codeName +
                " | Origem: " + source +
                " | Destino: " + destination +
                " | Data: " + date1 +
                " | Partida: " + hTakeOff +
                " | Chegada: " + hLanding +
                " | Passageiros: " + maxPassengers;
    }
}