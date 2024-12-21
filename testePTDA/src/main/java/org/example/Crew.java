package org.example;

import java.util.ArrayList;

public class Crew {

    private int id_CrewMember;
    private String name;
    private int phoneNumber;
    private String shift;
    private int experience;
    private ArrayList<Crew> crew;

    public Crew(int id_CrewMember, String name, int phoneNumber, String shift, int experience) {
        SetId_CrewMember(id_CrewMember);
        setName(name);
        setPhoneNumber(phoneNumber);
        setShift(shift);
        setExperience(experience);
    }

    public void SetId_CrewMember(int id_CrewMember) {
        if (id_CrewMember > 1 && id_CrewMember <= 999999999) {
            this.id_CrewMember = id_CrewMember;
        } else {
            throw new IllegalArgumentException("ID de membro de Tripulação inválido");
        }
    }

    public int getId_CrewMember() {
        return id_CrewMember;
    }

    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Nome de membro da Tripulação não pode ser vazio");
        }
    }

    public String getName() {
        return name;
    }

    public void setPhoneNumber(int phoneNumber) {
        if (phoneNumber >=  000000000 && phoneNumber <= 999999999) {
            this.phoneNumber = phoneNumber;
        } else {
            throw new IllegalArgumentException("Número de Telefone inválido");
        }
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setShift(String shift) {
        if (shift != null && !shift.trim().isEmpty()) {
            this.shift = shift;
        } else {
            throw new IllegalArgumentException("Turno não pode ser vazio");
        }
    }

    public String getShift() {
        return shift;
    }

    public void setExperience(int experience) {
        if (experience >= 0 && experience <= 50) {
            this.experience = experience;
        } else {
            throw new IllegalArgumentException("Anos de experiência inválidos");
        }
    }
    public int getExperience() {
        return experience;
    }

    static class Assistant extends Crew {

        public Assistant(int id_CrewMember, String name, int phoneNumber, String shift, int experience) {
            super(id_CrewMember, name, phoneNumber, shift,experience);
        }
    }

    static class Pilot extends Crew {
        private String rank;

        public Pilot(int id_CrewMember, String name, int phoneNumber, String shift, int experience) {
            super(id_CrewMember, name, phoneNumber, shift, experience);
            setRank(rank);
        }

        public void setRank(String rank) {
            if (rank != null && !rank.trim().isEmpty()) {
                this.rank = rank;
            } else {
                throw new IllegalArgumentException("Cargo não pode ser vazio");
            }
        }

        public String getRank() {
            return rank;
        }

    }

    public void addCrewMember(Crew crewMember) {
        crew.add(crewMember);
    }

    public void removeCrewMember(Crew crewMember) {
        crew.remove(crewMember);
    }

}
