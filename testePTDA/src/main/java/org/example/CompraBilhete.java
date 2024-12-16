package org.example;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class CompraBilhete extends JFrame {

    public CompraBilhete() {
        // Configurações da Janela Principal
        setTitle("Compra de Bilhete");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela

        // Criação do TabbedPane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Página 1: Seleção de Destino, Origem e Data
        JPanel panelDestinoOrigemData = new JPanel(new GridLayout(4, 2, 10, 10));

        // Labels
        JLabel labelOrigem = new JLabel("Origem:");
        JLabel labelDestino = new JLabel("Destino:");
        JLabel labelData = new JLabel("Data (dd/MM/yyyy):");

        // ComboBox para Origem e Destino
        String[] locais = {"Lisboa", "Porto", "Madrid", "Londres", "Paris"};
        JComboBox<String> comboOrigem = new JComboBox<>(locais);
        JComboBox<String> comboDestino = new JComboBox<>(locais);

        // Campo formatado para Data
        JFormattedTextField fieldData = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
        fieldData.setValue(new Date()); // Preenche com a data atual

        // Botão Próximo
        JButton btnProximo = getjButton(comboOrigem, comboDestino, fieldData);

        // Adicionar componentes ao painel
        panelDestinoOrigemData.add(labelOrigem);
        panelDestinoOrigemData.add(comboOrigem);
        panelDestinoOrigemData.add(labelDestino);
        panelDestinoOrigemData.add(comboDestino);
        panelDestinoOrigemData.add(labelData);
        panelDestinoOrigemData.add(fieldData);
        panelDestinoOrigemData.add(new JLabel()); // Espaço vazio
        panelDestinoOrigemData.add(btnProximo);

        // Adicionar a aba ao TabbedPane
        tabbedPane.addTab("Destino e Data", panelDestinoOrigemData);

        // Adiciona o TabbedPane à Janela Principal
        add(tabbedPane);

        // Exibir a Janela
        setVisible(true);
    }

    private JButton getjButton(JComboBox<String> comboOrigem, JComboBox<String> comboDestino, JFormattedTextField fieldData) {
        JButton btnProximo = new JButton("Próximo");
        btnProximo.addActionListener(e -> {
            // Validação simples
            if (Objects.equals(comboOrigem.getSelectedItem(), comboDestino.getSelectedItem())) {
                JOptionPane.showMessageDialog(this, "Origem e Destino não podem ser iguais!", "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Dados Validados:\nOrigem: " + comboOrigem.getSelectedItem() +
                        "\nDestino: " + comboDestino.getSelectedItem() +
                        "\nData: " + fieldData.getText(), "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                // Lógica para avançar à próxima aba (adicionada mais tarde)
            }
        });
        return btnProximo;
    }

    public static void main(String[] args) {
        new CompraBilhete();
    }
}
