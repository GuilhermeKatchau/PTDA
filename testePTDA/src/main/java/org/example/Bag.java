package org.example;

public class Bag {

    // Atributos para armazenar informações sobre a mala
    private int size; // Tamanho da mala (exemplo: dimensões aproximadas em cm)
    private double weight; // Peso da mala (em quilogramas)
    private int quantity; // Quantidade de malas associadas a um bilhete
    private int ticketId; // Identificador do bilhete associado à mala

    // Construtor para inicializar os atributos da mala
    public Bag(int size, double weight, int quantity, int ticketId) {
        this.size = size;
        this.weight = weight;
        this.quantity = quantity;
        this.ticketId = ticketId;
    }

    // Métodos getters e setters para aceder e alterar os valores dos atributos

    public int getSize() {
        return size; // Retorna o tamanho da mala
    }

    public void setSize(int size) {
        this.size = size; // Define ou altera o tamanho da mala
    }

    public double getWeight() {
        return weight; // Retorna o peso da mala
    }

    public void setWeight(double weight) {
        this.weight = weight; // Define ou altera o peso da mala
    }

    public int getQuantity() {
        return quantity; // Retorna a quantidade de malas
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity; // Define ou altera a quantidade de malas
    }

    public int getTicketId() {
        return ticketId; // Retorna o identificador do bilhete associado
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId; // Define ou altera o identificador do bilhete
    }

    // Método para calcular custos adicionais caso o peso exceda um limite
    public double calculateExtraCost(double weightLimit, double costPerKg) {
        // Se o peso for maior que o limite permitido, calcula o custo adicional
        return (weight > weightLimit) ? (weight - weightLimit) * costPerKg : 0;
    }

    // Método para exibir as informações da mala de forma legível
    @Override
    public String toString() {
        return "Bag [Tamanho: " + size + ", Peso: " + weight + "kg, Quantidade: " + quantity + ", ID Bilhete: " + ticketId + "]";
    }

}
