package org.example;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Passenger {

    private int id_Passenger; // id do passageiro
    private int age; // idade do passageiro
    private String name; // nome do passageiro
    private String email; // email do passageiro
    ArrayList<Passenger> passengers = new ArrayList<>(); // lista de passageiros

    // construtor da classe
    public Passenger(String name, int age, String email, int id_Passenger) {
        setId_Passenger(id_Passenger);
        setName(name);
        setAge(age);
        setEmail(email);
    }

    // define o id do passageiro
    public void setId_Passenger(int id_Passenger) {
        if (id_Passenger > 1 && id_Passenger <= 999999999) {
            this.id_Passenger = id_Passenger;
        } else {
            throw new IllegalArgumentException("id de passageiro invalido");
        }
    }

    // retorna o id do passageiro
    public int getId_Passenger() {
        return id_Passenger;
    }

    // define o nome do passageiro
    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("nome de passageiro nao pode ser vazio");
        }
    }

    // retorna o nome do passageiro
    public String getName() {
        return name;
    }

    // define a idade do passageiro
    public void setAge(int age) {
        if (age > 5 && age <= 200) {
            this.age = age;
        } else {
            throw new IllegalArgumentException("idade invalida");
        }
    }

    // retorna a idade do passageiro
    public int getAge() {
        return age;
    }

    // define o email do passageiro
    public void setEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (email != null && pattern.matcher(email).matches()) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("email invalido");
        }
    }

    // retorna o email do passageiro
    public String getEmail() {
        return email;
    }

    // adiciona um passageiro à lista
    public void addPassenger(Passenger passenger) {
        passengers.add(passenger);
        id_Passenger++;
    }

    // remove um passageiro da lista
    public void removePassenger(Passenger passenger) {
        passengers.remove(passenger);
        id_Passenger--;
    }

    // retorna uma representacao textual do passageiro
    @Override
    public String toString() {
        return "Passenger{" +
                "id_Passenger=" + id_Passenger +
                ", age=" + age +
                ", name='" + name + '\'' +
                ", email='" + email + '\''
                ;
    }

    /* metodo para comprar um bilhete (comentado)
    public boolean buyTicket(Ticket ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("o bilhete nao pode ser nulo.");
        }
        Flight flight = ticket.getFlight();
        boolean checkIn = false;
        Date hCheckIn = flight.gethTakeoff() - 1.00;

        if (automatic) {
            checkIn = true;
        } else if (!checkIn && hCheckIn < System.currentTimeMillis() / 3600000.0) {
            System.out.println("passageiro ausente");
        } else {
            System.out.println("check-in nao efetuado");
        }
        return checkIn;
    }
    */
}