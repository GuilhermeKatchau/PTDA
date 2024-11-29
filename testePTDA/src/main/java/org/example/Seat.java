package org.example;

public class Seat {

    private int id_Seat;
    private double price;
    private String place;

    public void setId_Seat(int id_Seat) {
        if (id_Seat > 100000000 && id_Seat <= 999999999) {
            this.id_Seat = id_Seat;
        } else {
            throw new IllegalArgumentException("ID de Assento inválido");
        }
    }

    public int getId_Seat() {
        return id_Seat;
    }

    public void setPlace(String place) {
        if (place != null && !place.trim().isEmpty()) {
            this.place = place;
        } else {
            throw new IllegalArgumentException("Lugar de assento não pode ser vazio");
        }
    }

    public String getPlace() {
        return place;
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

}
