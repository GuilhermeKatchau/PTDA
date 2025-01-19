package org.example;

public class CheckIn {

    private int hCheckIn; // hora do check-in
    private boolean checkIn; // status do check-in (true se realizado, false caso contrario)

    // retorna a hora do check-in
    public int gethCheckIn() {
        return hCheckIn;
    }

    // retorna o status do check-in
    public boolean isCheckIn() {
        return checkIn;
    }

    // define a hora do check-in
    public void sethCheckIn(int hCheckIn) {
        this.hCheckIn = hCheckIn;
    }

    // define o status do check-in
    public void setCheckIn(boolean checkIn) {
        this.checkIn = checkIn;
    }
}