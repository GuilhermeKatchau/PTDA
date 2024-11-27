public class Passenger {

    private int id_Passenger; // --> id_Passenger = id adicionar á classe Main
    private int priceTicket; // --> priceTicket = price adicionar á classe Main
    private int age;
    private String name;
    private String email;
    private int id_Ticket;
    private String destination;
    private String source;
    Ticket ticket = new Ticket(destination, source, id_Passenger, priceTicket);
    this.id_Ticket = ticket.getId_Ticket();


    public Passenger(int id_Passenger, int age, String name, String email) {
        setId_Passenger(id_Passenger);
        setName(name);
        setAge(age);
        setEmail(email);
    }

    public void setId_Passenger(int id_Passenger) {
        if (id_Passenger > 100000000 && id_Passenger <= 999999999) {
            this.id_Passenger = id_Passenger;
        } else {
            throw new IllegalArgumentException("ID de Passageiro inválido");
        }
    }

    public int getId_Passenger() {
        return id_Passenger;
    }

    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Nome de Passageiro não pode ser vazio");
        }
    }

    public String getName() {
        return name;
    }

    public void setAge(int age) {
        if (age > 5 && age <= 200) {
            this.age = age;
        } else {
            throw new IllegalArgumentException("Idade inválido");
        }
    }

    public int getAge() {
        return age;
    }

    public void setEmail(String email) {
        if (email != null && !email.trim().isEmpty()) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Email não pode ser vazio");
        }
    }

    public String getEmail() {
        return email;
    }

    public void addPassenger(Passenger passenger) {
        passengers.addPassenger(passenger);
        id_Passenger++;
    }
    public void removePassenger(Passenger passenger){
        passengers.remove(passenger);
        id_Passenger--;
    }

    public boolean buyTicket(Ticket ticket){
        private boolean checkIn = false;
        private double hCheckIn;
        if(automatic == true){
            checkIn = true;
            hCheckIn = hTakeoff - 1.00;
        }else{
            checkIn = false;
            System.out.println("Check-in não efetuado");
            }
        }else if(checkIn == false && hCheck < hTakeoff - 1.00){
            System.out.println("Passageiro ausente");
        }
        return checkIn;
}
