package org.example;

public class Crew {

    private int id_Crew;
    private String name;
    private int phoneNumber;
    private String shift;
    private int experience;

    public Crew(int id_Crew, String name, int phoneNumber, String shift, int experience) {
        setId_Crew(id_Crew);
        setName(name);
        setPhoneNumber(phoneNumber);
        setShift(shift);
        setExperience(experience);
    }

    public void setId_Crew(int id_Crew) {
        if (id_Crew > 100000000 && id_Crew <= 999999999) {
            this.id_Crew = id_Crew;
        } else {
            throw new IllegalArgumentException("ID de membro de Tripulação inválido");
        }
    }

    public int getId_Crew() {
        return id_Crew;
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
        if (phoneNumber >= 000000000 && phoneNumber <= 999999999) {
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

    class Assistant extends Crew {

        public Assistant(int id_Crew, String name, int phoneNumber, String shift, int experience) {
            super(id_Crew, name, phoneNumber, shift,experience);
        }
    }

    class Pilot extends Crew {
        private String rank;

        public Pilot(int id_Crew, String name, int phoneNumber, String shift, int experience) {
            super(id_Crew, name, phoneNumber, shift, experience);
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



}
