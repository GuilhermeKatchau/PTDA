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
    private JPanel panel1;
    private JButton[] seatButtons;
    public ClasseN3() {
        setTitle("Escolha o Assento");
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        panel1 = new JPanel();
        panel1.setLayout(new GridLayout(5, 1)); // 12 linhas de 1 coluna que conterá 2 subpainéis de botões

        seatButtons = new JButton[24];
        for (int i = 0; i < 24; i += 4) {
            // Crie dois sub-painéis (esquerda e direita) para cada linha
            JPanel linha = new JPanel();
            linha.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

            // Painel da esquerda com 2 botões
            JPanel painelEsquerda = new JPanel();
            painelEsquerda.setLayout(new GridLayout(2, 1, 0, 0));
            for (int j = 0; j < 2; j++) {
                seatButtons[i + j] = new JButton(String.valueOf(i + j + 1));
                painelEsquerda.add(seatButtons[i + j]);
            }

            // Painel da direita com 2 botões
            JPanel painelDireita = new JPanel();
            painelDireita.setLayout(new GridLayout(2, 1, 0, 0));
            for (int j = 2; j < 4; j++) {
                seatButtons[i + j] = new JButton(String.valueOf(i + j + 1));
                painelDireita.add(seatButtons[i + j]);
            }


            linha.add(painelEsquerda);
            linha.add(painelDireita);
            panel1.add(linha);
        }

        adicionarEventosBotoes();
        add(panel1, BorderLayout.CENTER);
        setVisible(true);
    }



    private void adicionarEventosBotoes() {
        for (int i = 0; i < seatButtons.length; i++) {
            int seatNumber = i + 1; // Números de 1 a 24
            seatButtons[i].addActionListener(e -> processarEscolhaAssento(seatNumber));
        }


    }

    private void processarEscolhaAssento(int numeroAssento) {
        JOptionPane.showMessageDialog(this, "Você escolheu o assento: " + numeroAssento);
    }
<<<<<<< HEAD

    public JPanel getPanel() {
        return panelSeat3;
    }

    public JButton[] getBotoesAssentos() {
        return botoesAssentos;
    }

=======
>>>>>>> refs/remotes/origin/main
    private Object[] enviarInformacoesAssento(JButton botao, int numeroAssento) {
        // Lógica para enviar as informações do botão e número do assento
        System.out.println("Assento selecionado: " + numeroAssento);
        return new Object[]{botao, numeroAssento};
    }
    private void saveSeat(int idTicket, int idSeat, double price,Class classe){
<<<<<<< HEAD

        Main.saveSeatInfo(idTicket, idSeat, price,classe);

=======
>>>>>>> refs/remotes/origin/main
        Main.saveSeatInfo(idTicket, idSeat, price, classe);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SkyBoundAdicionarAssento());
    }

    public JPanel getPanel() {
        return null;
    }
}
