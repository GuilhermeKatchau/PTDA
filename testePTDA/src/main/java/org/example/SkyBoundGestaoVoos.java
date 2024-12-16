package org.example;

import javax.swing.*;
import java.awt.*; // Importa GridLayout e BorderLayout

public class SkyBoundGestaoVoos extends JFrame {
    public SkyBoundGestaoVoos() {
        // Configurações da janela
        setTitle("Gestão de Voos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Criação painel do formulário
        JPanel formPanel = new JPanel(new GridLayout(4, 4, 10, 10));
        formPanel.add(new JLabel("ID do Avião:"));
        formPanel.add(new JTextField());

        formPanel.add(new JLabel("ID do Voo:"));
        formPanel.add(new JTextField());

        formPanel.add(new JLabel("Code Name:"));
        formPanel.add(new JTextField());

        formPanel.add(new JLabel("Cidade de Origem:"));
        formPanel.add(new JTextField());

        formPanel.add(new JLabel("Cidade de Destino:"));
        formPanel.add(new JTextField());

        formPanel.add(new JLabel("Limite de Passageiros:"));
        formPanel.add(new JTextField());

        // Campos de Data e Hora
        JSpinner tempoPartida = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor1 = new JSpinner.DateEditor(tempoPartida, "dd/MM/yyyy HH:mm");
        tempoPartida.setEditor(editor1);

        JSpinner tempoChegada = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor2 = new JSpinner.DateEditor(tempoChegada, "dd/MM/yyyy HH:mm");
        tempoChegada.setEditor(editor2);

        formPanel.add(new JLabel("Tempo de Partida:"));
        formPanel.add(tempoPartida);

        formPanel.add(new JLabel("Tempo de Chegada:"));
        formPanel.add(tempoChegada);

        // Adiciona o painel ao topo da janela
        add(formPanel, BorderLayout.NORTH);

        // Torna a janela visível
        setVisible(true);

        //Botão de submissão de voos
        JButton btnAdicionar = new JButton("Adicionar Voo");
        btnAdicionar.addActionListener(e -> JOptionPane.showMessageDialog(this, "Voo Adicionado!"));
        formPanel.add(btnAdicionar);

        //Mostra a lista dos voos cadastrados
        DefaultListModel<String> listaVoos = new DefaultListModel<>();
        JList<String> voosCadastrados = new JList<>(listaVoos);
        JScrollPane scrollPane = new JScrollPane(voosCadastrados);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Voos Cadastrados"));

        add(scrollPane, BorderLayout.CENTER);


    }

    public static void main(String[] args) {
        new SkyBoundGestaoVoos();
    }
}
