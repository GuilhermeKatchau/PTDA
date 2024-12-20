package org.example;

public class Seat {

    private int id_Seat;
    private double price;

    public void setId_Seat(int id_Seat) {
        if (id_Seat > 1 && id_Seat <= 999999999) {
            this.id_Seat = id_Seat;
        } else {
            throw new IllegalArgumentException("ID de Assento inválido");
        }
    }

    public int getId_Seat() {
        return id_Seat;
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
