package org.example;

public class Seat {

    private int id_Seat; // id do assento
    private double price; // preco do assento
    private Class seatClass; // referencia à classe do assento
    private boolean occupied; // indica se o assento está ocupado

    // define o id do assento
    public void setId_Seat(int id_Seat) {
        if (id_Seat >= 1 && id_Seat <= 999999999) {
            this.id_Seat = id_Seat;
        } else {
            throw new IllegalArgumentException("id de assento invalido");
        }
    }

    // retorna o id do assento
    public int getId_Seat() {
        return id_Seat;
    }

    // define o preco do assento
    public void setPrice(double price) {
        if (price > 100 && price <= 1000000) {
            this.price = price;
        } else {
            throw new IllegalArgumentException("preco invalido");
        }
    }

    // retorna o preco do assento
    public double getPrice() {
        return price;
    }

    // define se o assento está ocupado
    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    // retorna se o assento está ocupado
    public boolean getOccupied() {
        return occupied;
    }

    // retorna a classe do assento
    public Class getSeatClass() {
        return seatClass;
    }

    // define a classe do assento
    public void setSeatClass(Class seatClass) {
        this.seatClass = seatClass;
    }
}