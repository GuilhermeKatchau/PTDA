package org.example;

import javax.swing.*;
import java.awt.*;

public class ClasseN3 extends JFrame{
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JButton button5;
    private JButton button6;
    private JButton button7;
    private JButton button8;
    private JButton button9;
    private JButton button10;
    private JButton button11;
    private JButton button12;
    private JButton button13;
    private JButton button14;
    private JButton button15;
    private JButton button16;
    private JPanel panelSeat3;
    private JButton[] botoesAssentos;

    public ClasseN3() {
        setTitle("Escolha o Assento");
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        initPanel();
    }

    private void initPanel() {
        panelSeat3 = new JPanel();
        panelSeat3.setLayout(new GridLayout(5, 1)); // 12 linhas de 1 coluna que conterá 2 subpainéis de botões

        botoesAssentos = new JButton[16];
        for (int i = 0; i < 16; i += 4) {
            JPanel linha = new JPanel();
            linha.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

            JPanel painelEsquerda = new JPanel();
            painelEsquerda.setLayout(new GridLayout(2, 1, 0, 0));
            for (int j = 0; j < 2; j++) {
                botoesAssentos[i + j] = new JButton(String.valueOf(i + j + 1));
                painelEsquerda.add(botoesAssentos[i + j]);
            }

            JPanel painelDireita = new JPanel();
            painelDireita.setLayout(new GridLayout(2, 1, 0, 0));
            for (int j = 2; j < 4; j++) {
                botoesAssentos[i + j] = new JButton(String.valueOf(i + j + 1));
                painelDireita.add(botoesAssentos[i + j]);
            }

            linha.add(painelEsquerda);
            linha.add(painelDireita);
            panelSeat3.add(linha);
        }

        adicionarEventosBotoes();
    }

    private void adicionarEventosBotoes() {
        for (int i = 0; i < botoesAssentos.length; i++) {
            int numeroAssento = i + 1; // Números de 1 a 24
            botoesAssentos[i].addActionListener(e -> processarEscolhaAssento(numeroAssento));
        }
    }

    private void processarEscolhaAssento(int numeroAssento) {
        JOptionPane.showMessageDialog(this, "Você escolheu o assento: " + numeroAssento);
    }

    public JPanel getPanel() {
        return panelSeat3;
    }
    private Object[] enviarInformacoesAssento(JButton botao, int numeroAssento) {
        // Lógica para enviar as informações do botão e número do assento
        System.out.println("Assento selecionado: " + numeroAssento);
        return new Object[]{botao, numeroAssento};
    }
    private void saveSeat(int idTicket, int idSeat, double price,Class classe){
<<<<<<< HEAD
        Main.saveSeatInfo(idTicket, idSeat, price,classe);
=======
        Main.saveSeatInfo(idTicket, idSeat, price, classe);
>>>>>>> refs/remotes/origin/main
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SkyBoundAdicionarAssento());
    }

}
