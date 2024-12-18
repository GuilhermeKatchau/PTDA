package org.example;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.*;

public class CompraBilhete extends JFrame {
    private final JTabbedPane tabbedPane;
    private final CheckIn checkInData = new CheckIn();

    public CompraBilhete() {
        setTitle("Compra de Bilhete");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();

        tabDestinationSource();
        tabHourFlight();
        tabClassService(); // Este método agora está corretamente definido
        tabPassengerInfo();
        tabFinalize();

        add(tabbedPane);
        setVisible(true);
    }

    private void tabDestinationSource() {
        JPanel panelDestinoOrigemData = new JPanel(new GridLayout(4, 2, 10, 10));

        JLabel labelOrigem = new JLabel("Origem:");
        JLabel labelDestino = new JLabel("Destino:");
        JLabel labelData = new JLabel("Data (dd/MM/yyyy):");

        String[] locais = {"Lisboa", "Porto", "Madrid", "Londres", "Paris"};
        JComboBox<String> comboOrigem = new JComboBox<>(locais);
        JComboBox<String> comboDestino = new JComboBox<>(locais);

        JFormattedTextField fieldData = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
        fieldData.setValue(new Date());

        JButton btnProximo = new JButton("Próximo");
        btnProximo.addActionListener(e -> tabbedPane.setSelectedIndex(1));

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

    private void tabHourFlight() {
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

        JButton btnProximo = new JButton("Próximo");
        btnProximo.addActionListener(e -> {
            if (tableVoos.getSelectedRow() != -1) {
                tabbedPane.setSelectedIndex(2);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um voo!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        panelHoraVoo.add(scrollPane, BorderLayout.CENTER);
        panelHoraVoo.add(btnProximo, BorderLayout.SOUTH);

        tabbedPane.addTab("Hora e Voo", panelHoraVoo);
    }

    private void tabClassService() {
        JPanel panelClassService = new JPanel(new GridLayout(6, 1, 10, 10));
        JLabel labelClass = new JLabel("Escolha a Classe:");

        String[] classes = {"Luxuosa", "Executiva", "Econômica"};
        JComboBox<String> comboClass = new JComboBox<>(classes);

        JLabel labelService = new JLabel("Serviços Adicionais:");
        JCheckBox bagagemExtra = new JCheckBox("Bagagem Extra");
        JCheckBox refeicaoGourmet = new JCheckBox("Refeição Gourmet");

        JButton btnProximo = new JButton("Próximo");
        btnProximo.addActionListener(e -> tabbedPane.setSelectedIndex(3));

        panelClassService.add(labelClass);
        panelClassService.add(comboClass);
        panelClassService.add(labelService);
        panelClassService.add(bagagemExtra);
        panelClassService.add(refeicaoGourmet);
        panelClassService.add(btnProximo);

        tabbedPane.addTab("Classe e Serviços", panelClassService);
    }

    private void tabPassengerInfo() {
        JPanel panelPassageiro = new JPanel(new GridLayout(6, 2, 10, 10));

        JLabel labelName = new JLabel("Nome:");
        JTextField fieldName = new JTextField();

        JLabel labelAge = new JLabel("Idade:");
        JSpinner spinnerAge = new JSpinner(new SpinnerNumberModel(18, 1, 120, 1));

        JLabel labelEmail = new JLabel("Email:");
        JTextField fieldEmail = new JTextField();

        JLabel labelCheckIn = new JLabel("Check-in:");
        JRadioButton radioAutomatic = new JRadioButton("Automático");
        JRadioButton radioManual = new JRadioButton("Manual");
        ButtonGroup checkInGroup = new ButtonGroup();
        checkInGroup.add(radioAutomatic);
        checkInGroup.add(radioManual);

        JButton btnProximo = new JButton("Próximo");
        btnProximo.addActionListener(e -> tabbedPane.setSelectedIndex(4));

        panelPassageiro.add(labelName);
        panelPassageiro.add(fieldName);
        panelPassageiro.add(labelAge);
        panelPassageiro.add(spinnerAge);
        panelPassageiro.add(labelEmail);
        panelPassageiro.add(fieldEmail);
        panelPassageiro.add(labelCheckIn);
        panelPassageiro.add(radioAutomatic);
        panelPassageiro.add(new JLabel());
        panelPassageiro.add(radioManual);
        panelPassageiro.add(new JLabel());
        panelPassageiro.add(btnProximo);

        tabbedPane.addTab("Informação do Passageiro", panelPassageiro);
    }

    private void tabFinalize() {
        JPanel panelFinalize = new JPanel(new BorderLayout());
        JButton btnFinalize = new JButton("Finalizar Compra");

        btnFinalize.addActionListener(e -> JOptionPane.showMessageDialog(this, "Compra Finalizada com Sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE));
        panelFinalize.add(btnFinalize, BorderLayout.CENTER);

        tabbedPane.addTab("Finalizar", panelFinalize);
    }

    public static void main(String[] args) {
        new CompraBilhete();
    }
}
