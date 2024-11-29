package org.example;

public class Class {

    // Atributos para armazenar informações sobre a classe do voo
    private String className; // Nome da classe (exemplo: "Primeira", "Económica")
    private double price; // Preço associado à classe
    private int seatCapacity; // Capacidade total de lugares nessa classe
    private String services; // Descrição dos serviços disponíveis para a classe

    // Construtor para inicializar os atributos da classe
    public Class(String className, double price, int seatCapacity, String services) {
        this.className = className;
        this.price = price;
        this.seatCapacity = seatCapacity;
        this.services = services;
    }

    // Métodos getters e setters para aceder e alterar os valores dos atributos

    public String getClassName() {
        return className; // Retorna o nome da classe
    }

    public void setClassName(String className) {
        this.className = className; // Define ou altera o nome da classe
    }

    public double getPrice() {
        return price; // Retorna o preço da classe
    }

    public void setPrice(double price) {
        this.price = price; // Define ou altera o preço da classe
    }

    public int getSeatCapacity() {
        return seatCapacity; // Retorna a capacidade total de lugares
    }

    public void setSeatCapacity(int seatCapacity) {
        this.seatCapacity = seatCapacity; // Define ou altera a capacidade de lugares
    }

    public String getServices() {
        return services; // Retorna os serviços disponíveis na classe
    }

    public void setServices(String services) {
        this.services = services; // Define ou altera os serviços disponíveis
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

}
