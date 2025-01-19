package org.example;

import java.util.ArrayList;
import java.util.List;

public class Class {

    // Atributos para armazenar informações sobre a classe do voo
    private String className;
    private double price;
    private int seatCapacity;
    private int id; // Add this line
    static List<Class> classes = new ArrayList<>();
    private List<String> services;

    // Construtor para inicializar os atributos da classe
    public Class(String className, double price, int seatCapacity, List<String> services) {
        this.className = className;
        this.price = price;
        this.seatCapacity = seatCapacity;
        this.services = services;
    }

    // Métodos getters e setters para aceder e alterar os valores dos atributos
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSeatCapacity() {
        return seatCapacity;
    }

    public void setSeatCapacity(int seatCapacity) {
        this.seatCapacity = seatCapacity;
    }

    public List<String> getServices() {
        return services;
    }

    public void setServices(List<String> services) {
        this.services = services;
    }

    // Método para verificar se há lugares disponíveis
    public boolean hasAvailableSeats(int bookedSeats) {
        // Retorna verdadeiro se o número de lugares reservados for menor que a capacidade total
        return bookedSeats < seatCapacity;
    }

    // Método para exibir as informações da classe de forma legível
    @Override
    public String toString() {
        return "Class [Nome: " + className + ", Preço: " + price + ", Capacidade: " + seatCapacity + ", Serviços: " + services + "]";
    }

    public List<Class> getClasses() {
        return classes;
    }

    public void addClass(Class classe) {
        classes.add(classe);
    }

    public void removeClass(Class classe) {
        classes.remove(classe);
    }

    // Método para gerar números de assentos com base na capacidade de assentos
    public ArrayList<Integer> generateSeats() {
        ArrayList<Integer> seats = new ArrayList<>();
        for (int i = 1; i <= seatCapacity; i++) {
            seats.add(i); // Gera assentos de 1 até seatCapacity
        }
        return seats;
    }

    // Add this method
    public int getId() {
        return id;
    }

    // Add this method
    public void setId(int id) {
        this.id = id;
    }
}