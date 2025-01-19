package org.example;

import javax.swing.*;
import java.util.*;

import static java.lang.String.valueOf;

public class Flight {

    private int id_Flight; // id do voo
    private static int id_Airplane; // id do aviao
    private String codeName; // codigo do voo
    private String source; // origem do voo
    private String destination; // destino do voo
    private int maxPassengers; // limite maximo de passageiros
    private int nCompany; // numero da companhia (nao utilizado no codigo)
    private Date date1; // data do voo
    private Date hTakeOff; // hora de partida
    private Date hLanding; // hora de chegada

    protected static final ArrayList<Flight> flights = new ArrayList<>(); // lista estatica de voos

    // construtor para inicializar os atributos do voo
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

    // define o id do aviao
    private void setId_Airplane(int id_Airplane) {
        if (id_Airplane >= 1 && id_Airplane <= 999999999) {
            this.id_Airplane = id_Airplane;
        } else {
            throw new IllegalArgumentException("id de aviao invalido");
        }
    }

    // retorna o id do aviao
    public int getId_Airplane() {
        return id_Airplane;
    }

    // define a data do voo
    private void setDate1(Date date1) {
        if (date1 != null) {
            this.date1 = date1;
        } else {
            throw new IllegalArgumentException("data de voo invalida");
        }
    }

    // retorna a data do voo
    public Date getDate1() {
        return date1;
    }

    // define o limite maximo de passageiros
    public void setMaxPassengers(int maxPassengers) {
        if (maxPassengers > 0 && maxPassengers <= 900) {
            this.maxPassengers = maxPassengers;
        } else {
            throw new IllegalArgumentException("numero de passageiros invalido");
        }
    }

    // retorna o limite maximo de passageiros
    public int getMaxPassengers() {
        return maxPassengers;
    }

    // define o id do voo
    public void setId_Flight(int id_Flight) {
        if (id_Flight >= 1 || id_Flight <= 999999999) {
            this.id_Flight = id_Flight;
        } else {
            throw new IllegalArgumentException("id de voo invalido");
        }
    }

    // retorna o id do voo
    public int getId_Flight() {
        return id_Flight;
    }

    // define a hora de partida
    public void setHTakeoff(Date hTakeoff) {
        this.hTakeOff = hTakeoff;
    }

    // retorna a hora de partida
    public Date gethTakeoff() {
        return hTakeOff;
    }

    // define a hora de chegada
    public void setHLanding(Date hLanding) {
        this.hLanding = hLanding;
    }

    // retorna a hora de chegada
    public Date gethLanding() {
        return hLanding;
    }

    // define o destino do voo
    public void setDestination(String destination) {
        if (destination != null && !destination.trim().isEmpty()) {
            this.destination = destination;
        } else {
            throw new IllegalArgumentException("destino de voo nao pode ser vazio");
        }
    }

    // retorna o destino do voo
    public String getDestination() {
        return destination;
    }

    // define a origem do voo
    public void setSource(String source) {
        if (source != null && !source.trim().isEmpty()) {
            this.source = source;
        } else {
            throw new IllegalArgumentException("origem de voo nao pode ser vazia");
        }
    }

    // retorna a origem do voo
    public String getSource() {
        return source;
    }

    // define o codigo do voo
    public void setCodeName(String codeName) {
        if (codeName != null && !codeName.trim().isEmpty()) {
            this.codeName = codeName;
        } else {
            throw new IllegalArgumentException("nome de voo nao pode ser vazio");
        }
    }

    // retorna o codigo do voo
    public String getCodeName() {
        return codeName;
    }

    // adiciona um novo voo a lista de voos
    public static void addFlight(int id_Airplane, int id_Flight, int maxPassengers, Date date1, Date hTakeOff, Date hLanding, String destination, String source, String codename) {
        if (id_Flight == 0 || codename.isEmpty() || source.isEmpty() || destination.isEmpty()) {
            throw new IllegalArgumentException("por favor, preencha todos os campos!");
        }
        if (String.valueOf(date1).isEmpty()) {
            throw new IllegalArgumentException("por favor, preencha todos os campos!");
        }
        if (!hTakeOff.before(hLanding) || (hLanding.getTime() - hTakeOff.getTime()) < 60000) {
            throw new IllegalArgumentException("a data e hora de partida devem ser antes da data e hora de chegada, com pelo menos 1 minuto de diferenca.");
        }

        Flight newFlight = new Flight(id_Airplane, id_Flight, maxPassengers, date1, hTakeOff, hLanding, destination, source, codename);
        flights.add(newFlight);
        JOptionPane.showMessageDialog(null, "voo adicionado com sucesso!", "sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    // remove um voo da lista de voos
    public static void removeFlight(int flightIndex) {
        if (flightIndex >= 0 && flightIndex < flights.size()) {
            flights.remove(flightIndex);
            JOptionPane.showMessageDialog(null, "voo removido com sucesso!", "sucesso", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "indice de voo invalido!", "erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // retorna a lista de voos
    public static ArrayList<Flight> getFlights() {
        return new ArrayList<>(flights);
    }

    // remove voos de teste da lista de voos
    public static void removeTestFlights() {
        flights.removeIf(flight -> flight.getCodeName().startsWith("TEST_"));
    }

    // retorna uma representacao textual do voo
    @Override
    public String toString() {
        return "ID Aviao: " + id_Airplane +
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