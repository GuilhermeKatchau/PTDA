package org.example;

import java.util.ArrayList;
import java.util.Scanner;

public class Class {

    // Atributos para armazenar informações sobre a classe do voo
    private String className;
    private double price;
    private int seatCapacity;
    ArrayList<Class> classes;
    ArrayList<Service> services;

    // Construtor para inicializar os atributos da classe
    public Class(String className, double price, int seatCapacity,  ArrayList<Service> services) {
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

    public ArrayList<Service> getServices() {
        return services;
    }

    public void setServices(ArrayList<Service> services) {
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

    public ArrayList<Class> getClasses() {
        return classes;
    }

    public void newClass() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Qual é o nome da nova classe?");
        String className = scan.nextLine();
        System.out.println("Qual é o preço da nova classe?");
        int price = scan.nextInt();
        System.out.println("Qual é a capacidade de assentos do novo serviço?");
        int seatCapacity = scan.nextInt();
        ArrayList<Service> services = new ArrayList<>();
        System.out.println("Deseja adicionar serviços à nova classe? (sim/não)");
        String response = scan.nextLine();
        
        while (response.equals("sim")) {
            System.out.println("Qual é o nome do serviço?");
            String serviceName = scan.nextLine();
            System.out.println("Qual é o ID do serviço?");
            int serviceId = scan.nextInt();
            scan.nextLine();
            System.out.println("Qual é a descrição do serviço?");
            String serviceDescription = scan.nextLine();
            Service service = new Service(serviceName, serviceId, serviceDescription);
            services.add(service);
            System.out.println("Deseja adicionar outro serviço? (sim/não)");
            response = scan.nextLine();
        }

        Class newClass = new Class(className,price,seatCapacity,services);
        classes.add(newClass);
    }

    public void addClass(Class classe) {
        classes.add(classe);
    }
    public void removeClass(Class classe) {
        classes.remove(classe);
    }

}
