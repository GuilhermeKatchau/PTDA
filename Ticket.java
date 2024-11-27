public class Ticket {

    private int id_Ticket;
    private String destination;
    private String source;
    private double price;
    private boolean refundable;
    private boolean automatic;
    ArrayList<Ticket> tickets = new ArrayList<Ticket>();


    public Ticket(String destination, String source, int id_Ticket, double price) {
        setId_Ticket(id_Ticket);
        setSource(source);
        setDestination(destination);
        setPrice(price);
        refundable = true;
        automatic = true;
    }

    public void setId_Ticket(int id_Ticket) {
        if (id_Ticket > 100000000 && id_Ticket <= 999999999) {
            this.id_Ticket = id_Ticket;
        } else {
            throw new IllegalArgumentException("ID de Bilhete inválido");
        }
    }

    public int getId_Ticket() {
        return id_Ticket;
    }

    public void setSource(String source) {
        if (source != null && !source.trim().isEmpty()) {
            this.source = source;
        } else {
            throw new IllegalArgumentException("Origem de Voo não pode ser vazia");
        }
    }

    public String getSource() {
        return source;
    }

    public void setDestination(String destination) {
        if (destination != null && !destination.trim().isEmpty()) {
            this.destination = destination;
        } else {
            throw new IllegalArgumentException("Destino de Voo não pode ser vazio");
        }
    }

    public String getDestination() {
        return destination;
    }

    public void setPrice(double price) {
        if (price > 100 && price <= 1000000) {
            this.price = price;
        } else {
            throw new IllegalArgumentException("Preço inválido");
        }
    }

    public double getPrice() {
        return price;
    }

}
