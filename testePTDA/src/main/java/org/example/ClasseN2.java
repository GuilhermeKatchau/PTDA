package org.example;

import javax.swing.*;
import java.awt.*;

public class ClasseN2 extends JFrame{
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
    private JButton button17;
    private JButton button18;
    private JPanel panel1;
    private JButton button19;
    private JButton button20;
    private JButton button21;
    private JButton button22;
    private JButton button23;
    private JButton button24;
    private JButton[] botoesAssentos;

    public ClasseN2() {
        setTitle("Escolha o Assento");
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        panel1 = new JPanel();
        panel1.setLayout(new GridLayout(5, 1)); // 12 linhas de 1 coluna que conterá 2 subpainéis de botões

        botoesAssentos = new JButton[24];
        for (int i = 0; i < 24; i += 4) {
            // Crie dois sub-painéis (esquerda e direita) para cada linha
            JPanel linha = new JPanel();
            linha.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

            // Painel da esquerda com 2 botões
            JPanel painelEsquerda = new JPanel();
            painelEsquerda.setLayout(new GridLayout(2, 1, 0, 0));
            for (int j = 0; j < 2; j++) {
                botoesAssentos[i + j] = new JButton(String.valueOf(i + j + 1));
                painelEsquerda.add(botoesAssentos[i + j]);
            }

            // Painel da direita com 2 botões
            JPanel painelDireita = new JPanel();
            painelDireita.setLayout(new GridLayout(2, 1, 0, 0));
            for (int j = 2; j < 4; j++) {
                botoesAssentos[i + j] = new JButton(String.valueOf(i + j + 1));
                painelDireita.add(botoesAssentos[i + j]);
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
        for (int i = 0; i < botoesAssentos.length; i++) {
            int numeroAssento = i + 1; // Números de 1 a 24
            botoesAssentos[i].addActionListener(e -> processarEscolhaAssento(numeroAssento));
        }


    }

    private void processarEscolhaAssento(int numeroAssento) {
        JOptionPane.showMessageDialog(this, "Você escolheu o assento: " + numeroAssento);
    }
    private Object[] enviarInformacoesAssento(JButton botao, int numeroAssento) {
        // Lógica para enviar as informações do botão e número do assento
        System.out.println("Assento selecionado: " + numeroAssento);
        return new Object[]{botao, numeroAssento};
    }
    private void saveSeat(int idTicket, int idSeat, double price, Class classe){
        Main.saveSeatInfo(idTicket, idSeat, price, classe);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SkyBoundAdicionarAssento());
    }

}
