package org.example;

import javax.swing.*;

public class CompraBilhete extends JFrame {

    public CompraBilhete() {
        // Configurações da Janela Principal
        setTitle("Compra de Bilhete");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela

        // Criação do TabbedPane
        // TabbedPane para organizar as abas
        JTabbedPane tabbedPane = new JTabbedPane();

        // Adiciona o TabbedPane à Janela Principal
        add(tabbedPane);

        // Exibir a Janela
        setVisible(true);
    }

    public static void main(String[] args) {
        new CompraBilhete();
    }
}
