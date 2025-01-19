package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SkyBoundAdicionarAssento extends JFrame {
    private JPanel panelSeat1; // painel para os botoes dos assentos
    private JButton[] seatButtons; // array de botoes para os assentos
    private int idTicket; // id do ticket
    private String namePassenger; // nome do passageiro
    private double price; // preco do ticket
    private Class flightClass; // classe do voo
    private int idFlight; // id do voo
    private int numberOfPassengers; // numero de passageiros
    private boolean occupied; // indica se o assento esta ocupado

    // construtor atualizado para receber os parametros necessarios
    public SkyBoundAdicionarAssento(int idTicket, String namePassenger, double price, Class flightClass, int idFlight, int numberOfPassengers) {
        this.idTicket = idTicket;
        this.namePassenger = namePassenger;
        this.price = price;
        this.flightClass = flightClass;
        this.idFlight = idFlight;
        this.numberOfPassengers = numberOfPassengers;

        setTitle("escolha o assento - " + flightClass.getClassName());
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // configura o layout dos assentos com base na capacidade da classe
        int capacidade = flightClass.getSeatCapacity();
        panelSeat1 = new JPanel();
        panelSeat1.setLayout(new GridLayout(0, 3)); // 3 colunas para os assentos
        seatButtons = new JButton[capacidade];

        // cria os botoes para os assentos
        for (int i = 0; i < capacidade; i++) {
            seatButtons[i] = new JButton(String.valueOf(i + 1));
            seatButtons[i].setPreferredSize(new Dimension(80, 40));
            panelSeat1.add(seatButtons[i]);
        }

        adicionarEventosBotoes();
        add(panelSeat1, BorderLayout.CENTER);
    }

    // adiciona eventos aos botoes dos assentos
    private void adicionarEventosBotoes() {
        for (int i = 0; i < seatButtons.length; i++) {
            int seatNumber = i + 1; // numeros de 1 a capacidade
            seatButtons[i].addActionListener(e -> processarEscolhaAssento(seatNumber));
        }
    }

    // processa a escolha do assento
    private void processarEscolhaAssento(int numeroAssento) {
        JOptionPane.showMessageDialog(this, "voce escolheu o assento: " + numeroAssento);
        enviarInformacoesAssento(numeroAssento);
    }

    // envia as informacoes do assento selecionado
    private void enviarInformacoesAssento(int numeroAssento) {
        System.out.println("assento selecionado: " + numeroAssento);
        saveSeat(idTicket, idFlight, namePassenger, numeroAssento, price, occupied, flightClass);
    }

    // salva as informacoes do assento no banco de dados
    private void saveSeat(int idTicket, int idFlight, String namePassenger, int idSeat, double price, boolean occupied, Class classe) {
        Main.saveSeatInfo(String.valueOf(idTicket), namePassenger, idSeat, price, occupied, classe, idFlight);
    }

    // retorna o painel de assentos
    public JPanel getPanel() {
        return panelSeat1;
    }

    // retorna os botoes dos assentos
    public JButton[] getBotoesAssentos() {
        return seatButtons;
    }

    // metodo principal para teste
    public static void main(String[] args) {
        // exemplo de uso
        int idTicket = 1;
        String namePassenger = "joao silva";
        double price = 500.00;
        int idFlight = 123; // id do voo

        // exemplo de classe (deve ser carregada do banco de dados ou da lista de classes)
        ArrayList<String> servicos = new ArrayList<>();
        servicos.add("wi-fi");
        servicos.add("refeicao");
        Class flightClass = new Class("economica", price, 64, servicos);

        SwingUtilities.invokeLater(() -> new SkyBoundAdicionarAssento(idTicket, namePassenger, price, flightClass, idFlight, 1).setVisible(true));
    }
}