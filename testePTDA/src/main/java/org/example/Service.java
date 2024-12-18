package org.example;

import java.util.ArrayList;
import java.util.Scanner;

public class Service {

    private String name;
    private int id_Service;
    ArrayList <Service> services = new ArrayList<Service>();
    private String description;

    public Service(String name, int id_Service, String description) {
        setName(name);
        setId_Service(id_Service);
        setDescription(description);
    }

    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Nome do Serviço não pode ser vazio");
        }
    }

    public String getName() {
        return name;
    }

    public void setId_Service(int id_Service) {
        if (id_Service >= 1 && id_Service <= 999999999) {
            this.id_Service = id_Service;
        } else {
            throw new IllegalArgumentException("ID de Serviço inválido");
        }
    }

    public int getId_Service() {
        return id_Service;
    }
    public void setDescription(String description) {
        if (description != null && !description.trim().isEmpty()) {
            this.description = description;
        } else {
            throw new IllegalArgumentException("Descrição do Serviço não pode ser vazia");
        }
    }
    public String getDescription() {
        return description;
    }
    public void addService(Service service) {
        services.add(service);
        id_Service++;
    }
    public void removeService(Service service) {
        services.remove(service);
        id_Service--;
    }

    public void newService() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Qual é o nome do serviço?");
        String name = scan.nextLine();
        System.out.println("Qual é o Id do novo serviço?");
        int Id_Service = scan.nextInt();
        System.out.println("Qual é a descrição do novo serviço?");
        String description = scan.nextLine();
        Service newService = new Service(name,Id_Service,description);
        services.add(newService);
    }

    public Service displayService(Service service){
        System.out.println("Nome do Serviço: " + name);
        System.out.println("ID do Serviço: " + id_Service);
        System.out.println("Descrição: " + description);
        return service;
    }

}
