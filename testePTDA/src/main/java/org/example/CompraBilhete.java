package org.example;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class CompraBilhete extends JFrame {
    private final JTabbedPane tabbedPane;

    public CompraBilhete() {
        setTitle("Compra de Bilhete");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();

        criarAbaDestinoOrigemData();
        criarAbaHoraVoo();
        criarAbaClasseServicos();

        add(tabbedPane);
        setVisible(true);
    }

    private void criarAbaDestinoOrigemData() {
        JPanel panelDestinoOrigemData = new JPanel(new GridLayout(4, 2, 10, 10));

        JLabel labelOrigem = new JLabel("Origem:");
        JLabel labelDestino = new JLabel("Destino:");
        JLabel labelData = new JLabel("Data (dd/MM/yyyy):");

        String[] locais = {"Lisboa", "Porto", "Madrid", "Londres", "Paris"};
        JComboBox<String> comboOrigem = new JComboBox<>(locais);
        JComboBox<String> comboDestino = new JComboBox<>(locais);

        JFormattedTextField fieldData = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
        fieldData.setValue(new Date());

        // Criação do botão utilizando o método separado
        JButton btnProximo = criarBotaoProximoAba1(comboOrigem, comboDestino, fieldData);

        panelDestinoOrigemData.add(labelOrigem);
        panelDestinoOrigemData.add(comboOrigem);
        panelDestinoOrigemData.add(labelDestino);
        panelDestinoOrigemData.add(comboDestino);
        panelDestinoOrigemData.add(labelData);
        panelDestinoOrigemData.add(fieldData);
        panelDestinoOrigemData.add(new JLabel());
        panelDestinoOrigemData.add(btnProximo);

        tabbedPane.addTab("Destino e Data", panelDestinoOrigemData);
    }

    // Método separado para criar o botão Próximo da Aba 1
    private JButton criarBotaoProximoAba1(JComboBox<String> comboOrigem, JComboBox<String> comboDestino, JFormattedTextField fieldData) {
        JButton btnProximo = new JButton("Próximo");
        btnProximo.addActionListener(e -> {
            if (Objects.equals(comboOrigem.getSelectedItem(), comboDestino.getSelectedItem())) {
                JOptionPane.showMessageDialog(this, "Origem e Destino não podem ser iguais!", "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Dados Validados:\nOrigem: " + comboOrigem.getSelectedItem() +
                        "\nDestino: " + comboDestino.getSelectedItem() +
                        "\nData: " + fieldData.getText(), "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                tabbedPane.setSelectedIndex(1); // Avança para a próxima aba
            }
        });
        return btnProximo;
    }

    private void criarAbaHoraVoo() {
        JPanel panelHoraVoo = new JPanel(new BorderLayout());
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
        JButton btnProximo = criarBotaoProximo(tableVoos);

        panelHoraVoo.add(scrollPane, BorderLayout.CENTER);
        panelHoraVoo.add(btnProximo, BorderLayout.SOUTH);

        tabbedPane.addTab("Hora e Voo", panelHoraVoo);
    }

    private JButton criarBotaoProximo(JTable tableVoos) {
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
                tabbedPane.setSelectedIndex(2);
            }
        });
        return btnProximo;
    }

    private void criarAbaClasseServicos() {
        JPanel panelClasseServicos = new JPanel(new GridLayout(7, 1, 10, 10));

        JLabel labelClasse = new JLabel("Escolha a Classe:");
        String[] classes = {"Primeira (200€)", "Segunda (150€)", "Terceira (100€)"};
        JComboBox<String> comboClasse = new JComboBox<>(classes);

        JLabel labelServicos = new JLabel("Serviços Adicionais:");
        JCheckBox checkBagagem = new JCheckBox("Bagagem Extra (20€)");
        JCheckBox checkPrioridade = new JCheckBox("Prioridade de Embarque (10€)");
        JCheckBox checkRefeicao = new JCheckBox("Refeição Especial (15€)");

        JButton btnProximo = new JButton("Próximo");
        btnProximo.addActionListener(e -> {
            String classeSelecionada = (String) comboClasse.getSelectedItem();
            StringBuilder servicosSelecionados = new StringBuilder();

            if (checkBagagem.isSelected()) servicosSelecionados.append("Bagagem Extra, ");
            if (checkPrioridade.isSelected()) servicosSelecionados.append("Prioridade de Embarque, ");
            if (checkRefeicao.isSelected()) servicosSelecionados.append("Refeição Especial");

            String servicos = servicosSelecionados.toString().replaceAll(", $", "");
            JOptionPane.showMessageDialog(this, "Classe Selecionada: " + classeSelecionada +
                            "\nServiços Adicionais: " + (servicos.isEmpty() ? "Nenhum" : servicos),
                    "Resumo - Classe e Serviços", JOptionPane.INFORMATION_MESSAGE);

            tabbedPane.setSelectedIndex(3);
        });

        panelClasseServicos.add(labelClasse);
        panelClasseServicos.add(comboClasse);
        panelClasseServicos.add(labelServicos);
        panelClasseServicos.add(checkBagagem);
        panelClasseServicos.add(checkPrioridade);
        panelClasseServicos.add(checkRefeicao);
        panelClasseServicos.add(btnProximo);

        tabbedPane.addTab("Classe e Serviços", panelClasseServicos);
    }

    public static void main(String[] args) {
        new CompraBilhete();
    }
}
