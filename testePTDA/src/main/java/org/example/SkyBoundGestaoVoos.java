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

        // Adiciona o painel ao topo da janela
        add(formPanel, BorderLayout.NORTH);

        // Torna a janela visível
        setVisible(true);
    }

    public static void main(String[] args) {
        new SkyBoundGestaoVoos();
    }
}
