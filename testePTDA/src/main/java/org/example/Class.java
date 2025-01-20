package org.example;

import java.util.ArrayList;
import java.util.List;

public class Class {

    // atributos para armazenar informacoes sobre a classe do voo
    private String className; // nome da classe (ex: economica, executiva)
    private double price; // preco da classe
    private int seatCapacity; // capacidade de assentos da classe
    private int id; // identificador unico da classe
    static List<Class> classes = new ArrayList<>(); // lista estatica para armazenar todas as classes
    private List<String> services; // lista de servicos associados a classe

    // construtor para inicializar os atributos da classe
    public Class(String className, double price, int seatCapacity, ArrayList<String> services) {
        setClassName(className);
        setPrice(price);
        setSeatCapacity(seatCapacity);
        setServices(services);
    }

    // metodos getters e setters para aceder e alterar os valores dos atributos

    // retorna o nome da classe
    public String getClassName() {
        return className;
    }

    // define o nome da classe
    public void setClassName(String className) {
        this.className = className;
        if (className != null && !className.trim().isEmpty()) {
            this.className = className;
        } else {
            throw new IllegalArgumentException("Nome de Classe não pode ser vazio");
        }
    }

    // retorna o preco da classe
    public double getPrice() {
        return price;
    }

    // define o preco da classe
    public void setPrice(double price) {
        this.price = price;
        if (price < 0) {
            throw new IllegalArgumentException("Preço não pode ser negativo");
        } else {
            this.price = price;
        }
    }

    // retorna a capacidade de assentos da classe
    public int getSeatCapacity() {
        return seatCapacity;
    }

    // define a capacidade de assentos da classe
    public void setSeatCapacity(int seatCapacity) {
        this.seatCapacity = seatCapacity;
        if (seatCapacity < 0) {
            throw new IllegalArgumentException("Capacidade de assentos não pode ser negativa");
        } else {
            this.seatCapacity = seatCapacity;
        }
    }

    // retorna a lista de servicos da classe
    public List<String> getServices() {
        return services;
    }

    // define a lista de servicos da classe
    public void setServices(List<String> services) {
        if (services == null) {
            throw new NullPointerException("Tem de haver pelo menos 1 serviço!");
        } else {
            this.services = services;
        }
    }
    
    // metodo para verificar se ha lugares disponiveis
    public boolean hasAvailableSeats(int bookedSeats) {
        // retorna verdadeiro se o numero de lugares reservados for menor que a capacidade total
        return bookedSeats < seatCapacity;
    }

    // metodo para exibir as informacoes da classe de forma legivel
    @Override
    public String toString() {
        return "Class [Nome: " + className + ", Preco: " + price + ", Capacidade: " + seatCapacity + ", Servicos: " + services + "]";
    }

    // retorna a lista de todas as classes
    public List<Class> getClasses() {
        return classes;
    }

    // adiciona uma nova classe a lista de classes
    public void addClass(Class classe) {
        classes.add(classe);
    }

    // remove uma classe da lista de classes
    public void removeClass(Class classe) {
        classes.remove(classe);
    }

    // metodo para gerar numeros de assentos com base na capacidade de assentos
    public ArrayList<Integer> generateSeats() {
        ArrayList<Integer> seats = new ArrayList<>();
        for (int i = 1; i <= seatCapacity; i++) {
            seats.add(i); // gera assentos de 1 ate seatCapacity
        }
        return seats;
    }

    // retorna o id da classe
    public int getId() {
        return id;
    }

    // define o id da classe
    public void setId(int id) {
        this.id = id;
    }
}