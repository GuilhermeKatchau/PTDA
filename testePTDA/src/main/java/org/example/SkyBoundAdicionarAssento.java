package org.example;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.sql.*;

public class SkyBoundAdicionarAssento extends JFrame {
    private String assentoSelecionado;
    private JPanel panel1;
    private JButton button1, button2, button3, button4, button5, button6,
            button7, button8, button9, button10, button11, button12,
            button13, button14, button15, button16, button17, button18,
            button19, button20, button21, button22, button23, button24, button25,
            button26, button27, button28, button29, button30, button31, button32,
            button33, button34, button35, button36, button37, button38, button39,
            button40, button41, button42, button43, button44, button45, button46,
            button47, button48, button49, button50, button51, button52, button53,
            button54, button55, button56, button57, button58, button59, button60,
            button61, button62, button63, button64;

    private JButton[] botoesAssentos;

    public SkyBoundAdicionarAssento() {

        setTitle("Escolha o Assento");
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        panel1 = new JPanel();
        panel1.setLayout(new GridLayout(12, 1)); // 12 linhas de 1 coluna que conterá 2 subpainéis de botões

        botoesAssentos = new JButton[64];
        for (int i = 0; i < 64; i += 4) {
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
            JButton botao = botoesAssentos[i];
            int numeroAssento = i + 1; // Números de 1 a 64
            botoesAssentos[i].addActionListener(e -> processarEscolhaAssento(botao,numeroAssento));
        }


    }

    private void processarEscolhaAssento( JButton botao, int numeroAssento) {
        botao.setBackground(Color.orange);
        JOptionPane.showMessageDialog(this, "Você escolheu o assento: " + numeroAssento);
        enviarInformacoesAssento(botao, numeroAssento);
    }


    private Object[] enviarInformacoesAssento(JButton botao, int numeroAssento) {
        // Lógica para enviar as informações do botão e número do assento
        System.out.println("Assento selecionado: " + numeroAssento);
        return new Object[]{botao, numeroAssento};
    }
    private void saveSeat(int idTicket, int idSeat, double price,Class classe){
        Main.saveSeatInfo(idTicket, idSeat, price,classe);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SkyBoundAdicionarAssento());
    }

}
