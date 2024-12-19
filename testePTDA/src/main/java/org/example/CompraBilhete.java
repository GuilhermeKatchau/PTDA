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

        ArrayList<Class> availableClasses = getAvailableClasses(); // Método que retorna classes disponíveis
        JComboBox<Class> comboClass = new JComboBox<>(availableClasses.toArray(new Class[0]));

        JLabel labelService = new JLabel("Serviços Adicionais:");
        JPanel panelServices = new JPanel(new GridLayout(0, 1));
        JButton btnNext = new JButton("Próximo");

        comboClass.addActionListener(e -> {
            Class selectedClass = (Class) comboClass.getSelectedItem();
            panelServices.removeAll();
            if (selectedClass != null) {
                for (Service service : selectedClass.getServices()) {
                    JCheckBox serviceCheckBox = new JCheckBox(service.getName() + " (" + service.getDescription() + ")");
                    panelServices.add(serviceCheckBox);
                }
            }
            panelServices.revalidate();
            panelServices.repaint();
        });

        btnNext.addActionListener(e -> {
            Class classeSelecionada = (Class) comboClass.getSelectedItem();
            StringBuilder servicosSelecionados = new StringBuilder();

            // Coleta os serviços selecionados
            for (Component component : panelServices.getComponents()) {
                if (component instanceof JCheckBox && ((JCheckBox) component).isSelected()) {
                    servicosSelecionados.append(((JCheckBox) component).getText()).append(", ");
                }
            }

            String servicos = servicosSelecionados.toString().replaceAll(", $", "");
            JOptionPane.showMessageDialog(this, "Classe Selecionada: " + classeSelecionada +
                    "\nServiços Adicionais: " + (servicos.isEmpty() ? "Nenhum" : servicos), "Resumo - Classe e Serviços", JOptionPane.INFORMATION_MESSAGE);

            tabbedPane.setSelectedIndex(3); // Próxima aba
        });

        panelClassService.add(labelClass);
        panelClassService.add(comboClass);
        panelClassService.add(labelService);
        panelClassService.add(panelServices);
        panelClassService.add(btnNext);

        tabbedPane.addTab("Classe e Serviços", panelClassService);
    }

    private ArrayList<Class> getAvailableClasses() {
        ArrayList<Service> services1 = new ArrayList<>();
        services1.add(new Service("Bagagem Extra", 1, "Bagagem adicional para voos longos"));
        services1.add(new Service("Refeição Gourmet", 2, "Refeição premium durante o voo"));

        ArrayList<Service> services2 = new ArrayList<>();
        services2.add(new Service("Embarque Prioritário", 3, "Acesso prioritário ao embarque"));

        ArrayList<Class> classes = new ArrayList<>();
        classes.add(new Class("Luxuosa", 200.00, 10, services1));
        classes.add(new Class("Económica", 100.00, 50, services2));

        return classes;
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
        JRadioButton radioButtonAutomatic = new JRadioButton("Automático");
        JRadioButton radioButtonManual = new JRadioButton("Manual");
        ButtonGroup CheckIn = new ButtonGroup();
        CheckIn.add(radioButtonAutomatic);
        CheckIn.add(radioButtonManual);

        JButton btnNext = new JButton("Próximo");
        btnNext.addActionListener(e -> {
            String nome = fieldName.getText().trim();
            int idade = (int) spinnerAge.getValue();
            String email = fieldEmail.getText().trim();
            boolean isAutomatic = radioButtonAutomatic.isSelected();

            if (nome.isEmpty() || email.isEmpty() || CheckIn.getSelection() == null) {
                JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
            } else if (!email.contains("@")) {
                JOptionPane.showMessageDialog(this, "Insira um email válido!", "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                Passenger passageiro = new Passenger(123456789, idade, nome, email);
                checkInData.setCheckIn(isAutomatic);

                JOptionPane.showMessageDialog(this, "Passageiro Registrado:\n" + passageiro +
                        "\nCheck-in: " + (isAutomatic ? "Automático" : "Manual"), "Resumo", JOptionPane.INFORMATION_MESSAGE);
                tabbedPane.setSelectedIndex(4);
            }
        });

        panelPassageiro.add(labelName);
        panelPassageiro.add(fieldName);
        panelPassageiro.add(labelAge);
        panelPassageiro.add(spinnerAge);
        panelPassageiro.add(labelEmail);
        panelPassageiro.add(fieldEmail);
        panelPassageiro.add(labelCheckIn);
        panelPassageiro.add(radioButtonAutomatic);
        panelPassageiro.add(new JLabel());
        panelPassageiro.add(radioButtonManual);
        panelPassageiro.add(new JLabel());
        panelPassageiro.add(btnNext);

        tabbedPane.addTab("Informação do Passageiro", panelPassageiro);
    }

    private void tabFinalize() {
        JPanel panelFinalize = new JPanel(new BorderLayout());
        JButton btnFinalize = new JButton("Finalizar Compra");

        btnFinalize.addActionListener(e -> {
            Ticket ticket = new Ticket("Lisboa", "Porto", 123456, 150.00);
            JOptionPane.showMessageDialog(this, "Bilhete Criado:\n" + ticket, "Bilhete", JOptionPane.INFORMATION_MESSAGE);
        });
        panelFinalize.add(btnFinalize, BorderLayout.CENTER);

        tabbedPane.addTab("Finalizar", panelFinalize);
    }

    public static void main(String[] args) {
        new CompraBilhete();
    }
}
