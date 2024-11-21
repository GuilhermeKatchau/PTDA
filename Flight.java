public class Flight {

    private int id_Flight;
    private int maxPassengers;
    private int nCompany;
    private int hTakeoff;
    private int hLanding;
    private String destination;
    private String source;
    private String codeName;


    Public Flight(int id_Flight,int nCompany, int maxPassengers, int hTakeoff,
                  int hLanding, String destination, String source, String codeName) {
        setNPassengers(nPassengers);
        setid_Flight(id_Flight);
        setHTakeoff(hTakeoff);
        setHLanding(hLanding);
        setTrip(trip);
        setDestination(destination);
        nCompany = 0;
    }

        Public void setMaxPassengers(int maxPassengers) {
        if (maxPassengers > 0 && maxPassengers <= 900) {
            this.maxPassengers = maxPassengers;
        } else {
            throw new IllegalArgumentException("Número de passageiros inválido");
        }
    }

    public int getMaxPassengers() {
        return maxPassengers;
    }

    Public void setId_Flight (int id_Flight){
            if (id_Flight > 100000000 && id_Flight <= 999999999) {
                this.id_Flight = id_Flight;
            } else {
                throw new IllegalArgumentException("ID de Voo inválido");
            }
        }

    public int getId_Flight() {
        return id_Flight;
    }

    Public void setHTakeoff (int hTakeoff){
            if (hTakeoff > 0 && hTakeoff <= 24) {
                this.hTakeoff = hTakeoff;
            } else {
                throw new IllegalArgumentException("Hora de decolagem inválida");
            }
        }

    public int gethTakeoff() {
        return hTakeoff;
    }

    Public void setHLanding (int hLanding){
                    if (hLanding > 0 && hLanding <= 24) {
                        this.hLanding = hLanding;
                    } else {
                        throw new IllegalArgumentException("Hora de aterragem inválida");
                    }
                }

    public void sethLanding(int hLanding) {
        this.hLanding = hLanding;
    }

    public void setDestination (String destination){
            if (destination != null && !destination.trim().isEmpty()) {
                this.destination = destination;
            } else {
                throw new IllegalArgumentException("Destino de Voo não pode ser vazio");
            }
        }

    public String getDestination() {
        return destination;
    }

    public void setSource (String source){
            if (source != null && !source.trim().isEmpty()) {
                this.source = source;
            } else {
                throw new IllegalArgumentException("Origem de Voo não pode ser vazia");
            }
        }

    public String getSource() {
        return source;
    }

    public void setCodeName(String codeName) {
        if (codeName != null && !codeName.trim().isEmpty()) {
            this.codeName = codeName;
        } else {
            throw new IllegalArgumentException("Nome de Voo não pode ser vazio");
        }
    }

    public String getCodeName() {
        return codeName;
    }
}

