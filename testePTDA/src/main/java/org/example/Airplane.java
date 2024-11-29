package org.example;

import java.util.ArrayList;

public class Airplane {

    private int id_Airplane;
    private String destination;
    private String source;
    ArrayList<Airplane> airplanes = new ArrayList<>();

    public Airplane(int id_Airplane, String destination, String source) {
        setId_Airplane(id_Airplane);
        setSource(source);
        setDestination(destination);
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

    public void setId_Airplane(int id_Airplane) {
        if (id_Airplane > 100000000 && id_Airplane <= 999999999) {
            this.id_Airplane = id_Airplane;
        } else {
            throw new IllegalArgumentException("ID de Avião inválido");
        }
    }

    public int getId_Airplane() {
        return id_Airplane;
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


    // adiciona aviões á lista
    public void addAirplane(Airplane airplane) {
        airplanes.add(airplane);
        id_Airplane++;
    }
    //remove aviões da lista
    public  void removeAirplane(Airplane airplane){
        airplanes.remove(airplane);
        id_Airplane--;
    }
}
