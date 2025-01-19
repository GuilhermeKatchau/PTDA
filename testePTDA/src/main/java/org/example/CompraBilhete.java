package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

public class CompraBilhete extends JFrame {
    private JTabbedPane tabbedPane;
    private CheckIn checkInData = new CheckIn();
    private JPanel panelDestinoOrigemData;
    private String selectedSource;
    private String selectedDestination;
    private Flight selectedFlight;
    private JTable tableFlights;
    private ArrayList<Passenger> passengers = new ArrayList<>();
    private Class selectedClass;
    private ArrayList<Class> classes = new ArrayList<>();
    private int idTicket = new Random().nextInt(1000000);
    private Ticket ticket;
    private Seat selectedSeat = new Seat();
    private int numberOfPassengers=1;
    JSpinner numPassengersSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
    private JPanel panelAssentos = new JPanel(new GridLayout(0, 4, 10, 10)); // Para não dar Null Pointer Exception

    public CompraBilhete() {
        setTitle("Compra de Bilhete");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();

        tabDestinationSourceData();
        tabHourFlight();
        tabPassengerInfo();
        tabClassService();
        updateTabSeat(null);
        tabFinalize();

        add(tabbedPane);
        setVisible(true);
    }

    private Flight getFlightFromRow(int row) {
        DefaultTableModel model = (DefaultTableModel) tableFlights.getModel();
        int idAirplane = (int) model.getValueAt(row, 0);
        int idFlight = (int) model.getValueAt(row, 1);
        int maxPassengers = (int) model.getValueAt(row, 2);
        Date date1 = (Date) model.getValueAt(row, 3);
        Date hTakeOff = (Date) model.getValueAt(row, 4);
        Date hLanding = (Date) model.getValueAt(row, 5);
        String destination = (String) model.getValueAt(row, 6);
        String source = (String) model.getValueAt(row, 7);
        String codename = (String) model.getValueAt(row, 8);
        return new Flight(idAirplane, idFlight, maxPassengers, date1, hTakeOff, hLanding, destination, source, codename);
    }

    private void tabDestinationSourceData() {
        panelDestinoOrigemData = new JPanel(new GridLayout(5, 2, 10, 10));

        JLabel labelOrigem = new JLabel("Origem:");
        JLabel labelDestino = new JLabel("Destino:");
        JLabel labelData = new JLabel("Data (dd/MM/yyyy):");
        JLabel labelNumPassengers = new JLabel("Número de Passageiros:");

        String[] locais = {"Lisboa", "Porto", "Madrid", "Londres", "Paris"};
        JComboBox<String> comboOrigem = new JComboBox<>(locais);
        JComboBox<String> comboDestino = new JComboBox<>(locais);

        JFormattedTextField fieldData = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
        fieldData.setValue(new Date());

        numPassengersSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        if ((int) numPassengersSpinner.getValue() < 1 || (int) numPassengersSpinner.getValue()> 10) {
            JOptionPane.showMessageDialog(this, "O número de passageiros deve ser entre 1 e 10!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

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
        panelDestinoOrigemData.add(labelNumPassengers);
        panelDestinoOrigemData.add(numPassengersSpinner);
        panelDestinoOrigemData.add(new JLabel());
        panelDestinoOrigemData.add(btnProximo);

        tabbedPane.addTab("Destino e Data", panelDestinoOrigemData);
    }


    public ArrayList<Flight> filterFlights(String origem, String destino) {
        ArrayList<Flight> filteredFlights = new ArrayList<>();
        for (Flight flight : getAvailableFlights()) {
            if (flight.getSource().trim().equalsIgnoreCase(origem.trim()) &&
                    flight.getDestination().trim().equalsIgnoreCase(destino.trim())) {
                filteredFlights.add(flight);
            }
        }
        return filteredFlights;
    }

    ArrayList<Flight> getAvailableFlights() {
        return Flight.getFlights();
    }


    private void updateFlights() {
        DefaultTableModel model = (DefaultTableModel) tableFlights.getModel();
        model.setRowCount(0);

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")) {
            String sql = "SELECT f.id_plane, f.id, f.maxPassengers, f.date1, f.timeTakeOff, f.timeLanding, f.destination, f.source1, f.codename, " +
                    "f.maxPassengers - (SELECT COUNT(*) FROM seat WHERE id_flight = f.id) AS available_seats " +
                    "FROM flight f WHERE f.source1 = ? AND f.destination = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, selectedSource);
            stmt.setString(2, selectedDestination);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id_plane"),
                        rs.getInt("id"),
                        rs.getInt("maxPassengers"),
                        rs.getDate("date1"),
                        rs.getTime("timeTakeOff"),
                        rs.getTime("timeLanding"),
                        rs.getString("destination"),
                        rs.getString("source1"),
                        rs.getString("codename"),
                        rs.getInt("available_seats")
                };
                model.addRow(row);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar voos: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tabHourFlight() {
        JPanel panelHourFlight = new JPanel(new BorderLayout());

        String[] columns = {"ID Avião", "ID Voo", "Max Passageiros", "Data", "Hora Partida", "Hora Chegada", "Destino", "Origem", "Código", "Available Seats"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        tableFlights = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(tableFlights);

        JButton btnNext = new JButton("Próximo");
        btnNext.addActionListener(e -> {
            int selectedRow = tableFlights.getSelectedRow();
            if (selectedRow != -1) {
                selectedFlight = getFlightFromRow(selectedRow);
                tabbedPane.setSelectedIndex(2);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um voo!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        panelHourFlight.add(scrollPane, BorderLayout.CENTER);
        panelHourFlight.add(btnNext, BorderLayout.SOUTH);

        tabbedPane.addTab("Hora e Voo", panelHourFlight);
    }

    private void tabPassengerInfo() {
        JPanel panelPassageiro = new JPanel();
        panelPassageiro.setLayout(new BoxLayout(panelPassageiro, BoxLayout.Y_AXIS));

        // Arrays to hold passenger input components
        JTextField[] passengerName = new JTextField[(int) numPassengersSpinner.getValue()]; // Initialize with default value
        JSpinner[] passengerAge = new JSpinner[(int)numPassengersSpinner.getValue()];
        JTextField[] passengerEmail = new JTextField[(int)numPassengersSpinner.getValue()];
        JRadioButton[][] checkInOptions = new JRadioButton[(int)numPassengersSpinner.getValue()][2];

        for (int i = 0; i < (int)numPassengersSpinner.getValue(); i++) {
            JPanel panelPassenger = new JPanel(new GridLayout(0, 2, 10, 10));
            System.out.println("i: " + i);
            System.out.println("checkInOptions length: " + checkInOptions.length);
            System.out.println("checkInOptions[" + i + "] length: " + checkInOptions[i].length);

            passengerName[i] = new JTextField(20);
            panelPassenger.add(new JLabel("Nome:"));
            panelPassenger.add(passengerName[i]);

            passengerAge[i] = new JSpinner(new SpinnerNumberModel(18, 1, 120, 1));
            panelPassenger.add(new JLabel("Idade:"));
            panelPassenger.add(passengerAge[i]);

            passengerEmail[i] = new JTextField(20);
            panelPassenger.add(new JLabel("Email:"));
            panelPassenger.add(passengerEmail[i]);

            // Check-in options
            checkInOptions[i][0] = new JRadioButton("Automático");
            checkInOptions[i][1] = new JRadioButton("Manual");
            ButtonGroup group = new ButtonGroup();
            group.add(checkInOptions[i][0]);
            group.add(checkInOptions[i][1]);
            panelPassenger.add(new JLabel("Check-in:"));
            JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            radioPanel.add(checkInOptions[i][0]);
            radioPanel.add(checkInOptions[i][1]);
            panelPassenger.add(radioPanel);

            panelPassageiro.add(panelPassenger);
        }

        JButton btnNext = new JButton("Próximo");
        btnNext.addActionListener(e -> {
            passengers.clear();
            for (int i = 0; i < (int)numPassengersSpinner.getValue(); i++) {
                String name = passengerName[i].getText().trim();
                int age = (int) passengerAge[i].getValue();
                String email = passengerEmail[i].getText().trim();
                boolean isAutomatic = checkInOptions[i][0].isSelected();

                if (passengerName[i] == null || passengerAge[i] == null || passengerEmail[i] == null ||
                        checkInOptions[i][0] == null || checkInOptions[i][1] == null) {
                    JOptionPane.showMessageDialog(this, "Erro interno: Informações de passageiro não foram inicializadas corretamente!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!email.matches("^[a-zA-Z0-9][a-zA-Z0-9\\._%\\+\\-]{0,63}@[a-zA-Z0-9\\.\\-]+\\.[a-zA-Z]{2,30}$")) {
                    JOptionPane.showMessageDialog(this, "Insira um email válido para o passageiro " + (i + 1), "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int id = new Random().nextInt(1000000);
                Passenger p = new Passenger(name, age, email, id);
                passengers.add(p);
                // Save passenger data to database or wherever needed
                Main.SavePassengerData(name, age, email, id);
                checkInData.setCheckIn(isAutomatic);
            }
            JOptionPane.showMessageDialog(this, "Passageiros Registrados com Sucesso!", "Resumo", JOptionPane.INFORMATION_MESSAGE);
            tabbedPane.setSelectedIndex(3);
        });

        panelPassageiro.add(btnNext);
        tabbedPane.addTab("Informação do Passageiro", panelPassageiro);
    }


    private void tabClassService() {
        JPanel panelClassService = new JPanel(new GridLayout(6, 1, 10, 10));
        JLabel labelClass = new JLabel("Escolha a Classe:");

        ArrayList<Class> availableClasses = getAvailableClasses();
        JComboBox<Class> comboClass = new JComboBox<>(availableClasses.toArray(new Class[0]));
        comboClass.setSelectedIndex(0);
        selectedClass = (Class) comboClass.getSelectedItem();

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
                // Load available seats for the selected class
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

            // Collect selected services
            StringBuilder selectedServices = new StringBuilder();
            for (Component component : panelServices.getComponents()) {
                if (component instanceof JCheckBox && ((JCheckBox) component).isSelected()) {
                    selectedServices.append(((JCheckBox) component).getText()).append(", ");
                }
            }
            String services = selectedServices.toString().replaceAll(", $", "");
            JOptionPane.showMessageDialog(this, "Classe Selecionada: " + selectedClass + "\nServiços Adicionais: " + (services.isEmpty() ? "Nenhum" : services), "Resumo - Classe e Serviços", JOptionPane.INFORMATION_MESSAGE);

            // Update seat tab for the selected class
            updateTabSeat(selectedClass);
            tabbedPane.setSelectedIndex(4);
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
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar classes: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
        return classes;
    }

    private void updateTabSeat(Class selectedClass) {
        JPanel panelSeat = new JPanel();
        panelSeat.setLayout(new BoxLayout(panelSeat, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("Assentos disponíveis para a classe: " + (selectedClass != null ? selectedClass.getClassName() : "Nenhuma classe selecionada"));
        panelSeat.add(label);

        if (selectedClass != null) {
            panelAssentos.removeAll(); // Clear previous seats
            ArrayList<Integer> availableSeats = selectedClass.generateSeats();

            // Exclude already occupied seats
            ArrayList<Integer> occupiedSeats = getOccupiedSeats(selectedClass.getId());
            availableSeats.removeAll(occupiedSeats);

            for (int seatId : availableSeats) {
                JButton btnAssento = new JButton(String.valueOf(seatId));
                btnAssento.addActionListener(e -> {
                    selectedSeat.setId_Seat(seatId);
                    marcarAssentoComoOcupado(seatId);
                    carregarAssentosDisponiveis(selectedClass.getId());
                    btnAssento.setEnabled(false);
                    JOptionPane.showMessageDialog(this, "Assento selecionado: " + seatId, "Informação", JOptionPane.INFORMATION_MESSAGE);
                    btnAssento.setBackground(Color.ORANGE);
                    // You might need to store the seat for each passenger
                });
                panelAssentos.add(btnAssento);
            }

            JScrollPane scrollPane = new JScrollPane(panelAssentos);
            panelSeat.add(scrollPane);

            JButton btnNext = new JButton("Próximo");
            btnNext.addActionListener(e -> {
                if (selectedSeat.getId_Seat() == 0) {
                    JOptionPane.showMessageDialog(this, "Por favor, selecione um assento para cada passageiro!", "Erro", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Logic to handle multiple seats for multiple passengers
                    // You might need to loop through passengers and assign seats
                    // For simplicity, this example assigns one seat
                    marcarAssentoComoOcupado(selectedSeat.getId_Seat());
                    tabbedPane.setSelectedIndex(5);
                }
            });

            panelSeat.add(btnNext);
        }

        int index = tabbedPane.indexOfTab("Assento");
        if (index == -1) {
            tabbedPane.addTab("Assento", panelSeat);
        } else {
            tabbedPane.setComponentAt(index, panelSeat);
        }
        tabbedPane.revalidate();
        tabbedPane.repaint();
    }

    private void carregarAssentosDisponiveis(int classId) {
        panelAssentos.removeAll();
        ArrayList<Integer> allSeats = selectedClass.generateSeats();
        ArrayList<Integer> occupiedSeats = getOccupiedSeats(classId);

        // Filtra assentos Ocupados
        allSeats.removeAll(occupiedSeats);

        for (int seatId : allSeats) {
            JButton btnAssento = new JButton(String.valueOf(seatId));
            btnAssento.addActionListener(e -> {
                selectedSeat.setId_Seat(seatId);
                marcarAssentoComoOcupado(seatId);
                carregarAssentosDisponiveis(classId);
                btnAssento.setEnabled(false);
                JOptionPane.showMessageDialog(this, "Assento selecionado: " + seatId, "Informação", JOptionPane.INFORMATION_MESSAGE);
                btnAssento.setBackground(Color.ORANGE);
            });
            panelAssentos.add(btnAssento);
        }

        panelAssentos.revalidate();
        panelAssentos.repaint();
    }

    private ArrayList<Integer> getOccupiedSeats(int classId) {
        ArrayList<Integer> occupiedSeats = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")) {
            String sql = "SELECT id_Seat FROM seat WHERE class = ? AND id_flight = ? AND occupied = TRUE";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, selectedClass.getClassName());
            stmt.setInt(2, selectedFlight.getId_Flight());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int seatId = rs.getInt("id_Seat");
                occupiedSeats.add(seatId);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar assentos ocupados: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
        return occupiedSeats;
    }

    private void marcarAssentoComoOcupado(int idAssento) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")) {
            String sql = "UPDATE seat SET occupied = TRUE WHERE id_Seat = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idAssento);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao marcar assento como ocupado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tabFinalize() {
        JPanel panelFinalize = new JPanel(new BorderLayout());
        JButton btnFinalize = new JButton("Finalizar Compra");

        btnFinalize.addActionListener(e -> {
            boolean refundable = true;

            int idFlight = selectedFlight.getId_Flight();

            for (Passenger passenger : passengers) {
                // Assign seats to each passenger
                // For simplicity, using the same seat ID; in reality, each passenger should have their own seat
                Main.SaveTicket(String.valueOf(idTicket), passenger.getId_Passenger(), passenger.getName(), selectedSeat.getId_Seat(), selectedDestination, selectedClass.getPrice(), selectedSource, refundable, idFlight);
                Main.saveSeatInfo(String.valueOf(idTicket), passenger.getName(), selectedSeat.getId_Seat(), selectedSeat.getPrice(), selectedSeat.getOccupied(), selectedSeat.getSeatClass(), idFlight);
            }

            JOptionPane.showMessageDialog(this, "Compra Finalizada com Sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            // Reset tabs or proceed as needed
            tabbedPane.setSelectedIndex(0);
        });

        panelFinalize.add(btnFinalize, BorderLayout.CENTER);
        tabbedPane.addTab("Finalizar", panelFinalize);
    }

    private void resetDestinationSourceData() {
        ((JComboBox<String>) panelDestinoOrigemData.getComponent(1)).setSelectedIndex(0);
        ((JComboBox<String>) panelDestinoOrigemData.getComponent(3)).setSelectedIndex(0);
        ((JSpinner) panelDestinoOrigemData.getComponent(7)).setValue(1);
        numberOfPassengers = 1;
    }

    private void resetFlightHourTab() {
        DefaultTableModel model = (DefaultTableModel) tableFlights.getModel();
        model.setRowCount(0);
    }

    public static void main(String[] args) {
        new CompraBilhete();
    }
}
