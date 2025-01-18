package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.text.ParseException;
import java.util.Date;

public class CompraBilhete extends JFrame {
    private final JTabbedPane tabbedPane;
    private final CheckIn checkInData = new CheckIn();
    JPanel panelDestinoOrigemData;
    private String selectedSource;
    private String selectedDestination;
    private Flight selectedFlight;
    JTable tableFlights;
    private Passenger passenger;
    private Class selectedClass;
    private ArrayList<Class> classes = new ArrayList<>();
    private int idTicket = new Random().nextInt(1000000);
    private Ticket ticket;
    private Seat selectedSeat = new Seat(); // Inicializado para evitar NullPointerException
    private Class selectedSeatClass; // Inicializado para evitar NullPointerException

    public CompraBilhete() {
        setTitle("Compra de Bilhete");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();

        tabDestinationSource();
        tabHourFlight();
        tabClassService();
        tabSeat();
        tabPassengerInfo();
        tabFinalize();

        add(tabbedPane);
        setVisible(true);
    }

    private Flight getFlightFromRow(int row) {
        DefaultTableModel model = (DefaultTableModel) tableFlights.getModel();

        // Recupera os dados da linha selecionada na tabela
        int idAirplane = (int) model.getValueAt(row, 0); // ID do avião (correto: id_Airplane, não idAirplane)
        int idFlight = (int) model.getValueAt(row, 1); // ID do voo (correto: id, não id_Flight)
        int maxPassengers = (int) model.getValueAt(row, 2); // Número máximo de passageiros
        Date date1 = (Date) model.getValueAt(row, 3); // Data do voo
        Date hTakeOff = (Date) model.getValueAt(row, 4); // Hora de partida (correto: timeTakeOff, não hTakeOff)
        Date hLanding = (Date) model.getValueAt(row, 5); // Hora de chegada (correto: timeLanding, não hLanding)
        String destination = (String) model.getValueAt(row, 6); // Destino
        String source = (String) model.getValueAt(row, 7); // Origem
        String codename = (String) model.getValueAt(row, 8); // Código do voo

        // Retorna um novo objeto Flight com todos os dados
        return new Flight(idAirplane,idFlight ,maxPassengers, date1, hTakeOff, hLanding, destination, source, codename);
    }

    private void tabDestinationSource() {
        panelDestinoOrigemData = new JPanel(new GridLayout(4, 2, 10, 10));

        JLabel labelOrigem = new JLabel("Origem:");
        JLabel labelDestino = new JLabel("Destino:");
        JLabel labelData = new JLabel("Data (dd/MM/yyyy):");

        String[] locais = {"Lisboa", "Porto", "Madrid", "Londres", "Paris"};
        JComboBox<String> comboOrigem = new JComboBox<>(locais);
        JComboBox<String> comboDestino = new JComboBox<>(locais);

        JFormattedTextField fieldData = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
        fieldData.setValue(new Date());

        JButton btnProximo = new JButton("Próximo");
        btnProximo.addActionListener(e -> {
            selectedSource = comboOrigem.getSelectedItem().toString();
            selectedDestination = comboDestino.getSelectedItem().toString();

            if (selectedSource.equals(selectedDestination)) {
                JOptionPane.showMessageDialog(this, "A origem e o destino não podem ser iguais!", "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                updateFlights();
                tabbedPane.setSelectedIndex(1);
            }
        });

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

    private void updateFlights() {
        DefaultTableModel model = (DefaultTableModel) tableFlights.getModel();
        model.setRowCount(0); // Limpa os dados existentes na tabela

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")) {
            // Query corrigida para usar as colunas corretas
            String sql = "SELECT id_plane, id, maxPassengers, date1, timeTakeOff, timeLanding, destination, source1, codename FROM flight WHERE source1 = ? AND destination = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, selectedSource);
            stmt.setString(2, selectedDestination);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id_plane"), // ID do avião (correto: id_Airplane, não idAirplane)
                        rs.getInt("id"), // ID do voo (correto: id, não id_Flight)
                        rs.getInt("maxPassengers"), // Número máximo de passageiros
                        rs.getDate("date1"), // Data do voo
                        rs.getTime("timeTakeOff"), // Hora de partida (correto: timeTakeOff, não hTakeOff)
                        rs.getTime("timeLanding"), // Hora de chegada (correto: timeLanding, não hLanding)
                        rs.getString("destination"), // Destino
                        rs.getString("source1"), // Origem
                        rs.getString("codename") // Código do voo
                };
                model.addRow(row); // Adiciona a linha à tabela
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar voos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tabHourFlight() {
        JPanel panelHourFlight = new JPanel(new BorderLayout());

        // Colunas da tabela
        String[] columns = {"ID Avião", "ID Voo", "Max Passageiros", "Data", "Hora Partida", "Hora Chegada", "Destino", "Origem", "Código"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        tableFlights = new JTable(model);

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")) {
            String sql = "SELECT id_plane, id, maxPassengers, date1, timeTakeOff, timeLanding, destination, source1, codename FROM flight WHERE source1 = ? AND destination = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, selectedSource);
            stmt.setString(2, selectedDestination);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id_plane"), // ID do avião
                        rs.getInt("id"), // ID do voo
                        rs.getInt("maxPassengers"), // Número máximo de passageiros
                        rs.getDate("date1"), // Data do voo
                        rs.getTime("timeTakeOff"), // Hora de partida
                        rs.getTime("timeLanding"), // Hora de chegada
                        rs.getString("destination"), // Destino
                        rs.getString("source1"), // Origem
                        rs.getString("codename") // Código do voo
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(tableFlights);

        JButton btnNext = new JButton("Próximo");
        btnNext.addActionListener(e -> {
            int selectedRow = tableFlights.getSelectedRow();
            if (selectedRow != -1) {
                selectedFlight = getFlightFromRow(selectedRow); // Cria o objeto Flight
                tabbedPane.setSelectedIndex(2); // Avança para a próxima aba
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um voo!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        panelHourFlight.add(scrollPane, BorderLayout.CENTER);
        panelHourFlight.add(btnNext, BorderLayout.SOUTH);

        tabbedPane.addTab("Hora e Voo", panelHourFlight);
    }

    private void tabClassService() {
        JPanel panelClassService = new JPanel(new GridLayout(6, 1, 10, 10));
        JLabel labelClass = new JLabel("Escolha a Classe:");

        ArrayList<Class> availableClasses = getAvailableClasses();
        JComboBox<Class> comboClass = new JComboBox<>(availableClasses.toArray(new Class[0]));
        comboClass.setSelectedIndex(0); // Seleciona o primeiro item por padrão
        selectedClass = (Class) comboClass.getSelectedItem();

        // Botão para atualizar as classes
        JButton btnAtualizar = new JButton("Atualizar Classes");
        btnAtualizar.addActionListener(e -> {
            availableClasses.clear();
            availableClasses.addAll(getAvailableClasses());
            comboClass.setModel(new DefaultComboBoxModel<>(availableClasses.toArray(new Class[0])));
            JOptionPane.showMessageDialog(this, "Classes atualizadas com sucesso!", "Atualização", JOptionPane.INFORMATION_MESSAGE);
        });

        JLabel labelService = new JLabel("Serviços Adicionais:");
        JPanel panelServices = new JPanel(new GridLayout(0, 1));
        JButton btnNext = new JButton("Próximo");

        comboClass.addActionListener(e -> {
            selectedClass = (Class) comboClass.getSelectedItem();
            panelServices.removeAll();
            if (selectedClass != null) {
                for (String service : selectedClass.getServices()) {
                    JCheckBox serviceCheckBox = new JCheckBox(service);
                    panelServices.add(serviceCheckBox);
                }
                carregarAssentosDisponiveis(selectedClass.getId());
            }
            panelServices.revalidate();
            panelServices.repaint();
        });

        btnNext.addActionListener(e -> {
            selectedClass = (Class) comboClass.getSelectedItem();
            if (selectedClass == null) {
                JOptionPane.showMessageDialog(tabbedPane, "Por favor, selecione uma classe!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            StringBuilder selectedServices = new StringBuilder();
            for (Component component : panelServices.getComponents()) {
                if (component instanceof JCheckBox && ((JCheckBox) component).isSelected()) {
                    selectedServices.append(((JCheckBox) component).getText()).append(", ");
                }
            }

            String services = selectedServices.toString().replaceAll(", $", "");
            JOptionPane.showMessageDialog(this, "Classe Selecionada: " + selectedClass +
                    "\nServiços Adicionais: " + (services.isEmpty() ? "Nenhum" : services), "Resumo - Classe e Serviços", JOptionPane.INFORMATION_MESSAGE);

            tabbedPane.setSelectedIndex(3);
        });

        panelClassService.add(labelClass);
        panelClassService.add(comboClass);
        panelClassService.add(btnAtualizar);
        panelClassService.add(labelService);
        panelClassService.add(panelServices);
        panelClassService.add(btnNext);

        tabbedPane.addTab("Classe e Serviços", panelClassService);
    }

    private ArrayList<Class> getAvailableClasses() {
        ArrayList<Class> classes = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")) {
            String sql = "SELECT * FROM class";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String nome = rs.getString("nome");
                double preco = rs.getDouble("price");
                int capacidade = rs.getInt("capacity");
                ArrayList<String> servicos = new ArrayList<>(Arrays.asList(rs.getString("services").split(",")));

                Class novaClasse = new Class(nome, preco, capacidade, servicos);
                classes.add(novaClasse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar classes: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
        return classes;
    }

    private void carregarAssentosDisponiveis(int classId) {
        ArrayList<Integer> assentosDisponiveis = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")) {
            String sql = "SELECT id_Seat FROM seat WHERE class = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, classId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                assentosDisponiveis.add(rs.getInt("id_Seat"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar assentos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }

        System.out.println("Assentos disponíveis para a classe " + classId + ": " + assentosDisponiveis);
    }

    private void tabSeat() {
        JPanel panelSeatPlaceholder = new JPanel();
        JLabel placeholderLabel = new JLabel("Por favor, selecione uma classe primeiro.");
        panelSeatPlaceholder.add(placeholderLabel);

        tabbedPane.addTab("Assento", panelSeatPlaceholder);
    }

    private void tabPassengerInfo() {
        JPanel panelPassageiro = new JPanel(new GridLayout(6, 2, 10, 10));

        JLabel labelName = new JLabel("Nome:");
        JTextField passengerName = new JTextField();

        JLabel labelAge = new JLabel("Idade:");
        JSpinner passengerAge = new JSpinner(new SpinnerNumberModel(18, 1, 120, 1));

        JLabel labelEmail = new JLabel("Email:");
        JTextField passengerEmail = new JTextField();

        JLabel labelCheckIn = new JLabel("Check-in:");
        JRadioButton radioButtonAutomatic = new JRadioButton("Automático");
        JRadioButton radioButtonManual = new JRadioButton("Manual");
        ButtonGroup CheckIn = new ButtonGroup();
        CheckIn.add(radioButtonAutomatic);
        CheckIn.add(radioButtonManual);

        JButton btnNext = new JButton("Próximo");
        btnNext.addActionListener(e -> {
            String name = passengerName.getText().trim();
            int age = (int) passengerAge.getValue();
            String email = passengerEmail.getText().trim();
            boolean isAutomatic = radioButtonAutomatic.isSelected();

            if (name.isEmpty() || email.isEmpty() || CheckIn.getSelection() == null) {
                JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
            } else if (!email.matches("^[a-zA-Z0-9][a-zA-Z0-9\\._%\\+\\-]{0,63}@[a-zA-Z0-9\\.\\-]+\\.[a-zA-Z]{2,30}$")) {
                JOptionPane.showMessageDialog(this, "Insira um email válido!", "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                int id = new Random().nextInt(1000000);
                passenger = new Passenger(name, age, email, id);
                Main.SavePassengerData(name, age, email, id);
                checkInData.setCheckIn(isAutomatic);

                JOptionPane.showMessageDialog(this, "Passageiro Registrado:\n" + passenger.toString() +
                        "\nCheck-in: " + (isAutomatic ? "Automático" : "Manual"), "Resumo", JOptionPane.INFORMATION_MESSAGE);
                tabbedPane.setSelectedIndex(5);
            }
        });

        panelPassageiro.add(labelName);
        panelPassageiro.add(passengerName);
        panelPassageiro.add(labelAge);
        panelPassageiro.add(passengerAge);
        panelPassageiro.add(labelEmail);
        panelPassageiro.add(passengerEmail);
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
            ticket = new Ticket("Lisboa", "Porto", idTicket, 150.00);
            boolean refundable = true;

            Main.SaveTicket(passenger.getId_Passenger(), selectedDestination, ticket.getPrice(), selectedSource, refundable, idTicket);

            if (selectedSeat.getId_Seat() != 0 && selectedSeatClass != null) {
                Main.saveSeatInfo(idTicket, selectedSeat.getId_Seat(), selectedSeat.getPrice(), selectedSeatClass);
                System.out.println("Assento salvo com sucesso!");
            } else {
                System.out.println("Nenhum assento foi selecionado.");
            }

            JOptionPane.showMessageDialog(this, "Bilhete Criado:\n" + ticket.toString(), "Bilhete", JOptionPane.INFORMATION_MESSAGE);
            tabbedPane.setSelectedIndex(0);
        });
        panelFinalize.add(btnFinalize, BorderLayout.CENTER);

        tabbedPane.addTab("Finalizar", panelFinalize);
    }

    public static void main(String[] args) {
        new CompraBilhete();
    }
}