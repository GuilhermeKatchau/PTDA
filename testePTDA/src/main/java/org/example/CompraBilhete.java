package org.example;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
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
        tabClassService();
        tabPassengerInfo();
        tabFinalize();

        add(tabbedPane);
        setVisible(true);
    }

    // Método auxiliar para adicionar componentes ao painel
    private void addComponentToPanel(JPanel painel, Component componente1, Component componente2) {
        painel.add(componente1);
        painel.add(componente2);
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

        JButton btnProximo = nextButtonTab(comboOrigem, comboDestino, fieldData);

        // Usar método auxiliar para adicionar componentes
        addComponentToPanel(panelDestinoOrigemData, labelOrigem, comboOrigem);
        addComponentToPanel(panelDestinoOrigemData, labelDestino, comboDestino);
        addComponentToPanel(panelDestinoOrigemData, labelData, fieldData);
        addComponentToPanel(panelDestinoOrigemData, new JLabel(), btnProximo);

        tabbedPane.addTab("Destino e Data", panelDestinoOrigemData);
    }

    private JButton nextButtonTab(JComboBox<String> comboOrigem, JComboBox<String> comboDestino, JFormattedTextField fieldData) {
        JButton btnProximo = new JButton("Próximo");
        btnProximo.addActionListener(e -> {
            if (Objects.equals(comboOrigem.getSelectedItem(), comboDestino.getSelectedItem())) {
                JOptionPane.showMessageDialog(this, "Origem e Destino não podem ser iguais!", "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Dados Validados:\nOrigem: " + comboOrigem.getSelectedItem() +
                        "\nDestino: " + comboDestino.getSelectedItem() +
                        "\nData: " + fieldData.getText(), "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                tabbedPane.setSelectedIndex(1);
            }
        });
        return btnProximo;
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
        JButton btnProximo = nextButton(tableVoos);

        panelHoraVoo.add(scrollPane, BorderLayout.CENTER);
        panelHoraVoo.add(btnProximo, BorderLayout.SOUTH);

        tabbedPane.addTab("Hora e Voo", panelHoraVoo);
    }

    private JButton nextButton(JTable tableVoos) {
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

    private void tabClassService() {
        JPanel panelClassServices = new JPanel(new GridLayout(7, 1, 10, 10));

        JLabel labelClass = new JLabel("Escolha a Classe:");
        ArrayList<Class> availableClasses = getAvailableClasses(); // Método que retorna classes disponíveis
        JComboBox<Class> comboClass = new JComboBox<>(availableClasses.toArray(new Class[0]));

        JLabel labelServices = new JLabel("Serviços Adicionais:");
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
            Class selectedClass = (Class) comboClass.getSelectedItem();
            StringBuilder selectedServices = new StringBuilder();

            for (Component component : panelServices.getComponents()) {
                if (component instanceof JCheckBox && ((JCheckBox) component).isSelected()) {
                    selectedServices.append(((JCheckBox) component).getText()).append(", ");
                }
            }

            String servicos = selectedServices.toString().replaceAll(", $", "");
            JOptionPane.showMessageDialog(this, "Classe Selecionada: " + selectedClass +
                            "\nServiços Adicionais: " + (servicos.isEmpty() ? "Nenhum" : servicos),
                    "Resumo - Classe e Serviços", JOptionPane.INFORMATION_MESSAGE);

            tabbedPane.setSelectedIndex(3);
        });

        panelClassServices.add(labelClass);
        panelClassServices.add(comboClass);
        panelClassServices.add(labelServices);
        panelClassServices.add(panelServices);
        panelClassServices.add(btnNext);

        tabbedPane.addTab("Classe e Serviços", panelClassServices);
    }

    // Método auxiliar para retornar classes disponíveis
    private ArrayList<Class> getAvailableClasses() {
        ArrayList<Service> services1 = new ArrayList<>();
        services1.add(new Service("Bagagem Extra", 1, "Bagagem adicional para voos longos"));
        services1.add(new Service("Refeição Gourmet", 2, "Refeição premium durante o voo"));

        ArrayList<Service> services2 = new ArrayList<>();
        services2.add(new Service("Embarque Prioritário", 3, "Acesso prioritário ao embarque"));

        ArrayList<Class> classes = new ArrayList<>();
        classes.add(new Class("Luxuosa", 200.00, 10, services1));
        classes.add(new Class("Econômica", 100.00, 50, services2));

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
            String name = fieldName.getText().trim();
            int age = (int) spinnerAge.getValue();
            String email = fieldEmail.getText().trim();
            boolean isAutomatic = radioButtonAutomatic.isSelected();

            if (name.isEmpty() || email.isEmpty() || CheckIn.getSelection() == null) {
                JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
            } else if (!email.contains("@")) {
                JOptionPane.showMessageDialog(this, "Insira um email válido!", "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                //VEJAM PFF se podem meter o metodo getNextNumTicket aqui nesta classe.
               int id = Main.getNextNumTicket("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793");
               //isto e info que vai para a bd
                Main.SavePassengerData(name, age, email, id); // Passa esse id para salvar os dados

                checkInData.setCheckIn(isAutomatic);
                checkInData.sethCheckIn(isAutomatic ? 1 : 0);
                JOptionPane.showMessageDialog(this, "Informações do Passageiro:\nNome: " + name +
                                "\nIdade: " + age + "\nEmail: " + email + "\nCheck-in: " + (isAutomatic ? "Automático" : "Manual"),
                        "Resumo - Informação do Passageiro", JOptionPane.INFORMATION_MESSAGE);
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
        String checkInType = checkInData.isCheckIn() ? "Automático" : "Manual";

        btnFinalize.addActionListener(e -> {
            Ticket ticket = new Ticket("Lisboa", "Porto", 123456, 150.00);
            JOptionPane.showMessageDialog(this, "Bilhete Criado:\n" + ticket,
                    "Bilhete\n" + "Check-in: " + checkInType, JOptionPane.INFORMATION_MESSAGE);
        });

        panelFinalize.add(btnFinalize, BorderLayout.CENTER);
        tabbedPane.addTab("Resumo", panelFinalize);
    }

    public static void main(String[] args) {
        new CompraBilhete();
    }
}
