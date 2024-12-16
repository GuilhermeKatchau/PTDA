package org.example;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class CompraBilhete extends JFrame {
    private JTabbedPane tabbedPane;

    public CompraBilhete() {
        // Configurações da Janela Principal
        setTitle("Compra de Bilhete");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela

        // Criação do TabbedPane
        tabbedPane = new JTabbedPane();

        // Adicionar páginas ao TabbedPane
        criarAbaDestinoOrigemData();
        criarAbaHoraVoo();

        // Adicionar o TabbedPane à Janela Principal
        add(tabbedPane);

        // Exibir a Janela
        setVisible(true);
    }

    // Aba 1: Seleção de Destino, Origem e Data
    private void criarAbaDestinoOrigemData() {
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
        JButton btnProximo = new JButton("Próximo");
        btnProximo.addActionListener(e -> {
            // Validação simples
            if (Objects.equals(comboOrigem.getSelectedItem(), comboDestino.getSelectedItem())) {
                JOptionPane.showMessageDialog(this, "Origem e Destino não podem ser iguais!", "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Dados Validados:\nOrigem: " + comboOrigem.getSelectedItem() +
                        "\nDestino: " + comboDestino.getSelectedItem() +
                        "\nData: " + fieldData.getText(), "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                tabbedPane.setSelectedIndex(1); // Avança para a próxima aba
            }
        });

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
    }

    // Aba 2: Seleção de Hora e Voo
    private void criarAbaHoraVoo() {
        JPanel panelHoraVoo = new JPanel(new BorderLayout());

        // Tabela com a lista de voos disponíveis
        String[] colunas = {"Hora", "Origem", "Destino"};
        Object[][] voos = {
                {"10:00", "Lisboa", "Porto"},
                {"12:30", "Lisboa", "Madrid"},
                {"14:00", "Porto", "Londres"},
                {"16:30", "Madrid", "Paris"},
                {"18:00", "Londres", "Lisboa"}
        };
        JTable tableVoos = new JTable(voos, colunas);
        JScrollPane scrollPane = new JScrollPane(tableVoos);

        // Botão Próximo
        JButton btnProximo = new JButton("Próximo");
        btnProximo.addActionListener(e -> {
            int linhaSelecionada = tableVoos.getSelectedRow();
            if (linhaSelecionada == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um voo para continuar!", "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                String hora = (String) tableVoos.getValueAt(linhaSelecionada, 0);
                String origem = (String) tableVoos.getValueAt(linhaSelecionada, 1);
                String destino = (String) tableVoos.getValueAt(linhaSelecionada, 2);
                JOptionPane.showMessageDialog(this, "Voo Selecionado:\nHora: " + hora +
                        "\nOrigem: " + origem + "\nDestino: " + destino, "Voo Confirmado", JOptionPane.INFORMATION_MESSAGE);
                // Avançar para a próxima aba (será adicionada mais tarde)
            }
        });

        // Adicionar componentes à aba
        panelHoraVoo.add(scrollPane, BorderLayout.CENTER);
        panelHoraVoo.add(btnProximo, BorderLayout.SOUTH);

        // Adicionar a aba ao TabbedPane
        tabbedPane.addTab("Hora e Voo", panelHoraVoo);
    }

    public static void main(String[] args) {
        new CompraBilhete();
    }
}
