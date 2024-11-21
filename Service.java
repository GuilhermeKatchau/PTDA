public class Service {

    private String name;
    private int id_Service;

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
        if (id_Service > 100000000 && id_Service <= 999999999) {
            this.id_Service = id_Service;
        } else {
            throw new IllegalArgumentException("ID de Serviço inválido");
        }
    }

    public int getId_Service() {
        return id_Service;
    }
}
