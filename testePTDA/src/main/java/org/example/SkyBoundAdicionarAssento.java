package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SkyBoundAdicionarAssento extends JFrame {
    private JPanel panelSeat1;
    private JButton[] seatButtons;
    private int idTicket; // ID do ticket
    private String namePassenger; // Nome do passageiro
    private double price; // Preço do ticket
    private Class flightClass; // Classe do voo
    private int idFlight; // ID do voo
    private int numberOfPassengers;
    private boolean occupied;

    // Construtor atualizado para receber os parâmetros necessários
    public SkyBoundAdicionarAssento(int idTicket, String namePassenger, double price, Class flightClass, int idFlight, int numberOfPassengers) {
        this.idTicket = idTicket;
        this.namePassenger = namePassenger;
        this.price = price;
        this.flightClass = flightClass;
        this.idFlight = idFlight;
        this.numberOfPassengers = numberOfPassengers;

        setTitle("Escolha o Assento - " + flightClass.getClassName());
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Configura o layout dos assentos com base na capacidade da classe
        int capacidade = flightClass.getSeatCapacity();
        panelSeat1 = new JPanel();
        panelSeat1.setLayout(new GridLayout(0, 3)); // 3 colunas para os assentos
        seatButtons = new JButton[capacidade];

        // Cria os botões para os assentos
        for (int i = 0; i < capacidade; i++) {
            seatButtons[i] = new JButton(String.valueOf(i + 1));
            seatButtons[i].setPreferredSize(new Dimension(80, 40));
            panelSeat1.add(seatButtons[i]);
        }

        adicionarEventosBotoes();
        add(panelSeat1, BorderLayout.CENTER);
    }

    private void adicionarEventosBotoes() {
        for (int i = 0; i < seatButtons.length; i++) {
            int seatNumber = i + 1; // Números de 1 a capacidade
            seatButtons[i].addActionListener(e -> processarEscolhaAssento(seatNumber));
        }
    }

    private void processarEscolhaAssento(int numeroAssento) {
        JOptionPane.showMessageDialog(this, "Você escolheu o assento: " + numeroAssento);
        enviarInformacoesAssento(numeroAssento);
    }

    private void enviarInformacoesAssento(int numeroAssento) {
        // Lógica para enviar as informações do botão e número do assento
        System.out.println("Assento selecionado: " + numeroAssento);
        saveSeat(idTicket, idFlight, namePassenger, numeroAssento, price,occupied, flightClass);
    }

    private void saveSeat(int idTicket, int idFlight, String namePassenger, int idSeat, double price,boolean occupied, Class classe) {
        Main.saveSeatInfo(String.valueOf(idTicket), namePassenger, idSeat, price,occupied, classe, idFlight);
    }

    public JPanel getPanel() {
        return panelSeat1;
    }

    public JButton[] getBotoesAssentos() {
        return seatButtons;
    }

    public static void main(String[] args) {
        // Exemplo de uso
        int idTicket = 1;
        String namePassenger = "João Silva";
        double price = 500.00;
        int idFlight = 123; // ID do voo

        // Exemplo de classe (deve ser carregada do banco de dados ou da lista de classes)
        ArrayList<String> servicos = new ArrayList<>();
        servicos.add("Wi-Fi");
        servicos.add("Refeição");
        Class flightClass = new Class("Econômica", price, 64, servicos);

        SwingUtilities.invokeLater(() -> new SkyBoundAdicionarAssento(idTicket, namePassenger, price, flightClass, idFlight, 1).setVisible(true));
    }
}