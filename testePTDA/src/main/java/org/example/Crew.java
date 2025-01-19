package org.example;

import java.util.ArrayList;

public class Crew {

    private int id_CrewMember; // id do membro da tripulacao
    private String name; // nome do membro da tripulacao
    private int phoneNumber; // numero de telefone do membro da tripulacao
    private String shift; // turno do membro da tripulacao
    private int experience; // anos de experiencia do membro da tripulacao
    private ArrayList<Crew> crew; // lista de membros da tripulacao

    // construtor para inicializar os atributos da tripulacao
    public Crew(int id_CrewMember, String name, String shift, int experience) {
        SetId_CrewMember(id_CrewMember);
        setName(name);
        setPhoneNumber(phoneNumber);
        setShift(shift);
        setExperience(experience);
    }

    // define o id do membro da tripulacao
    public void SetId_CrewMember(int id_CrewMember) {
        if (id_CrewMember > 1 && id_CrewMember <= 999999999) {
            this.id_CrewMember = id_CrewMember;
        } else {
            throw new IllegalArgumentException("id de membro de tripulacao invalido");
        }
    }

    // retorna o id do membro da tripulacao
    public int getId_CrewMember() {
        return id_CrewMember;
    }

    // define o nome do membro da tripulacao
    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("nome de membro da tripulacao nao pode ser vazio");
        }
    }

    // retorna o nome do membro da tripulacao
    public String getName() {
        return name;
    }

    // define o numero de telefone do membro da tripulacao
    public void setPhoneNumber(int phoneNumber) {
        if (phoneNumber >= 000000000 && phoneNumber <= 999999999) {
            this.phoneNumber = phoneNumber;
        } else {
            throw new IllegalArgumentException("numero de telefone invalido");
        }
    }

    // retorna o numero de telefone do membro da tripulacao
    public int getPhoneNumber() {
        return phoneNumber;
    }

    // define o turno do membro da tripulacao
    public void setShift(String shift) {
        if (shift != null && !shift.trim().isEmpty()) {
            this.shift = shift;
        } else {
            throw new IllegalArgumentException("turno nao pode ser vazio");
        }
    }

    // retorna o turno do membro da tripulacao
    public String getShift() {
        return shift;
    }

    // define os anos de experiencia do membro da tripulacao
    public void setExperience(int experience) {
        if (experience >= 0 && experience <= 50) {
            this.experience = experience;
        } else {
            throw new IllegalArgumentException("anos de experiencia invalidos");
        }
    }

    // retorna os anos de experiencia do membro da tripulacao
    public int getExperience() {
        return experience;
    }

    // classe interna para assistentes de voo
    static class Assistant extends Crew {

        // construtor para inicializar um assistente de voo
        public Assistant(int id_CrewMember, String name, String shift, int experience) {
            super(id_CrewMember, name, shift, experience);
        }
    }

    // classe interna para pilotos
    static class Pilot extends Crew {
        private String rank; // cargo do piloto

        // construtor para inicializar um piloto
        public Pilot(int id_CrewMember, String name, String shift, int experience, String rank) {
            super(id_CrewMember, name, shift, experience);
            setRank(rank);
        }

        // define o cargo do piloto
        public void setRank(String rank) {
            System.out.println("valor recebido no setRank: '" + rank + "'");
            this.rank = rank.trim();
        }

        // retorna o cargo do piloto
        public String getRank() {
            return rank;
        }
    }

    // adiciona um membro da tripulacao a lista
    public void addCrewMember(Crew crewMember) {
        crew.add(crewMember);
    }

    // remove um membro da tripulacao da lista
    public void removeCrewMember(Crew crewMember) {
        crew.remove(crewMember);
    }
}