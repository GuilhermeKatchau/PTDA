package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import static org.example.Flight.flights;

public class CompraBilhete extends JFrame {
    private JTabbedPane tabbedPane; // painel com abas
    private CheckIn checkInData = new CheckIn(); // dados de check-in
    private JPanel panelDestinoOrigemData; // painel para origem, destino e data
    private String selectedSource; // origem selecionada
    private String selectedDestination; // destino selecionado
    private Flight selectedFlight; // voo selecionado
    private JTable tableFlights; // tabela de voos
    private List<Passenger> passengers = new ArrayList<>(); // lista de passageiros
    private Class selectedClass; // classe selecionada
    private List<Class> classes = new ArrayList<>(); // lista de classes disponiveis
    private int idTicket = new Random().nextInt(1000000); // id do bilhete gerado aleatoriamente
    private Ticket ticket; // bilhete
    private List<Seat> selectedSeats = new ArrayList<>(); // lista de assentos selecionados
    private int numberOfPassengers; // numero de passageiros
    private JSpinner numPassengersSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1)); // spinner para selecionar numero de passageiros
    private JPanel panelAssentos = new JPanel(new GridLayout(0, 4, 10, 10)); // painel de assentos

    private List<JPanel> passengerPanels = new ArrayList<>(); // paineis de informacao dos passageiros
    private List<JTextField> passengerNames = new ArrayList<>(); // campos de nome dos passageiros
    private List<JSpinner> passengerAges = new ArrayList<>(); // campos de idade dos passageiros
    private List<JTextField> passengerEmails = new ArrayList<>(); // campos de email dos passageiros
    private List<ButtonGroup> checkInGroups = new ArrayList<>(); // grupos de botoes para check-in

    private JComboBox<Class> classComboBox; // combo box para selecionar a classe
    private JPanel servicePanel; // painel de servicos adicionais

    public CompraBilhete() {
        setTitle("Compra de Bilhete"); // titulo da janela
        setSize(800, 800); // tamanho da janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // fecha a aplicacao ao fechar a janela
        setLocationRelativeTo(null); // centraliza a janela

        tabbedPane = new JTabbedPane(); // inicializa o painel de abas

        tabDestinationSourceData(); // adiciona a aba de origem, destino e data
        tabHourFlight(); // adiciona a aba de selecao de voo
        updateTabPassengerInfo(); // adiciona a aba de informacao dos passageiros
        tabClassService(); // adiciona a aba de selecao de classe e servicos
        updateTabSeat(null); // adiciona a aba de selecao de assentos
        tabFinalize(); // adiciona a aba de finalizacao

        add(tabbedPane); // adiciona o painel de abas a janela
        setVisible(true); // torna a janela visivel
    }

    // obtem o voo a partir de uma linha da tabela
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

    // cria a aba de origem, destino e data
    private void tabDestinationSourceData() {
        panelDestinoOrigemData = new JPanel(new GridLayout(5, 2, 10, 10));

        JLabel labelOrigem = new JLabel("Origem:");
        JLabel labelDestino = new JLabel("Destino:");
        JLabel labelData = new JLabel("Data (dd/MM/yyyy):");
        JLabel labelNumPassengers = new JLabel("Numero de Passageiros:");

        String[] locais = {"Lisboa", "Porto", "Madrid", "Londres", "Paris"};
        JComboBox<String> comboOrigem = new JComboBox<>(locais);
        JComboBox<String> comboDestino = new JComboBox<>(locais);

        JFormattedTextField fieldData = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
        fieldData.setValue(new Date());

        JButton btnProximo = new JButton("Proximo");
        btnProximo.addActionListener(e -> {
            selectedSource = comboOrigem.getSelectedItem().toString();
            selectedDestination = comboDestino.getSelectedItem().toString();
            numberOfPassengers = (int) numPassengersSpinner.getValue();

            // valida o numero de passageiros
            if (numberOfPassengers < 1 || numberOfPassengers > 10) {
                JOptionPane.showMessageDialog(this, "O numero de passageiros deve ser entre 1 e 10!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (selectedSource.equals(selectedDestination)) {
                JOptionPane.showMessageDialog(this, "A origem e o destino nao podem ser iguais!", "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                updateFlights();
                // atualiza a aba de informacao dos passageiros
                updateTabPassengerInfo();
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

    // atualiza a lista de voos disponiveis
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

    // cria a aba de selecao de voo
    private void tabHourFlight() {
        JPanel panelHourFlight = new JPanel(new BorderLayout());

        String[] columns = {"ID Aviao", "ID Voo", "Max Passageiros", "Data", "Hora Partida", "Hora Chegada", "Destino", "Origem", "Codigo", "Available Seats"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableFlights = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(tableFlights);

        JButton btnNext = new JButton("Proximo");
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

    // atualiza a aba de informacao dos passageiros
    private void updateTabPassengerInfo() {
        JPanel panelPassageiro = new JPanel();
        panelPassageiro.setLayout(new BoxLayout(panelPassageiro, BoxLayout.Y_AXIS));

        // limpa listas anteriores
        passengerPanels.clear();
        passengerNames.clear();
        passengerAges.clear();
        passengerEmails.clear();
        checkInGroups.clear();

        // cria os campos para cada passageiro
        for (int i = 0; i < numberOfPassengers; i++) {
            JPanel panelPassenger = new JPanel(new GridLayout(0, 2, 10, 10));

            JTextField nameField = new JTextField(20);
            passengerNames.add(nameField);
            panelPassenger.add(new JLabel("Nome:"));
            panelPassenger.add(nameField);

            JSpinner ageSpinner = new JSpinner(new SpinnerNumberModel(18, 1, 120, 1));
            passengerAges.add(ageSpinner);
            panelPassenger.add(new JLabel("Idade:"));
            panelPassenger.add(ageSpinner);

            JTextField emailField = new JTextField(20);
            passengerEmails.add(emailField);
            panelPassenger.add(new JLabel("Email:"));
            panelPassenger.add(emailField);

            JRadioButton autoCheckIn = new JRadioButton("Automatico");
            autoCheckIn.setActionCommand("Automatico"); // define o actionCommand
            JRadioButton manualCheckIn = new JRadioButton("Manual");
            manualCheckIn.setActionCommand("Manual"); // define o actionCommand
            ButtonGroup group = new ButtonGroup();
            group.add(autoCheckIn);
            group.add(manualCheckIn);
            checkInGroups.add(group);
            panelPassenger.add(new JLabel("Check-in:"));
            JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            radioPanel.add(autoCheckIn);
            radioPanel.add(manualCheckIn);
            panelPassenger.add(radioPanel);

            passengerPanels.add(panelPassenger);
            panelPassageiro.add(panelPassenger);
        }

        JButton btnNext = new JButton("Proximo");
        btnNext.addActionListener(e -> {
            passengers.clear();
            for (int i = 0; i < numberOfPassengers; i++) {
                // verifica se os indices estao dentro dos limites das listas
                if (i >= passengerNames.size() || i >= passengerAges.size() || i >= passengerEmails.size() || i >= checkInGroups.size()) {
                    JOptionPane.showMessageDialog(this, "Erro ao processar informacoes do passageiro " + (i + 1), "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String name = passengerNames.get(i).getText().trim();
                int age = (int) passengerAges.get(i).getValue();
                String email = passengerEmails.get(i).getText().trim();
                boolean isAutomatic = false;

                // verifica qual botao foi selecionado
                if (checkInGroups.get(i).getSelection() != null) {
                    isAutomatic = checkInGroups.get(i).getSelection().getActionCommand().equals("Automatico");
                }

                // validacao dos campos
                if (name.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos para o passageiro " + (i + 1), "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!email.matches("^[a-zA-Z0-9][a-zA-Z0-9\\._%\\+\\-]{0,63}@[a-zA-Z0-9\\.\\-]+\\.[a-zA-Z]{2,30}$")) {
                    JOptionPane.showMessageDialog(this, "Insira um email valido para o passageiro " + (i + 1), "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int id = new Random().nextInt(1000000);
                Passenger p = new Passenger(name, age, email, id);
                passengers.add(p);
                checkInData.setCheckIn(isAutomatic);
                Main.SavePassengerData(name, age, email, id);
            }
            JOptionPane.showMessageDialog(this, "Passageiros Registrados com Sucesso!", "Resumo", JOptionPane.INFORMATION_MESSAGE);
            tabbedPane.setSelectedIndex(3); // avanca para a proxima aba
        });

        panelPassageiro.add(btnNext);

        // atualiza a aba de informacao do passageiro
        int index = tabbedPane.indexOfTab("Informacao do Passageiro");
        if (index == -1) {
            tabbedPane.addTab("Informacao do Passageiro", panelPassageiro);
        } else {
            tabbedPane.setComponentAt(index, panelPassageiro);
        }
        tabbedPane.revalidate();
        tabbedPane.repaint();
    }

    // cria a aba de selecao de classe e servicos
    private void tabClassService() {
        JPanel panelClassService = new JPanel(new GridLayout(6, 1, 10, 10));
        JLabel labelClass = new JLabel("Escolha a Classe:");

        List<Class> availableClasses = getAvailableClasses();
        JComboBox<Class> comboClass = new JComboBox<>(availableClasses.toArray(new Class[0]));
        comboClass.setSelectedIndex(0);
        selectedClass = (Class) comboClass.getSelectedItem();

        JButton btnAtualizar = new JButton("Atualizar Classes");
        btnAtualizar.addActionListener(e -> {
            availableClasses.clear();
            availableClasses.addAll(getAvailableClasses());
            comboClass.setModel(new DefaultComboBoxModel<>(availableClasses.toArray(new Class[0])));
            JOptionPane.showMessageDialog(this, "Classes atualizadas com sucesso!", "Atualizacao", JOptionPane.INFORMATION_MESSAGE);
        });

        JLabel labelService = new JLabel("Servicos Adicionais:");
        JPanel panelServices = new JPanel(new GridLayout(0, 1));
        JButton btnNext = new JButton("Proximo");

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
            JOptionPane.showMessageDialog(this, "Classe Selecionada: " + selectedClass + "\nServicos Adicionais: " + (services.isEmpty() ? "Nenhum" : services), "Resumo - Classe e Servicos", JOptionPane.INFORMATION_MESSAGE);

            updateTabSeat(selectedClass);
            tabbedPane.setSelectedIndex(4);
        });

        panelClassService.add(labelClass);
        panelClassService.add(comboClass);
        panelClassService.add(btnAtualizar);
        panelClassService.add(labelService);
        panelClassService.add(panelServices);
        panelClassService.add(btnNext);

        tabbedPane.addTab("Classe e Servicos", panelClassService);
    }

    // obtem as classes disponiveis
    private List<Class> getAvailableClasses() {
        List<Class> classes = new ArrayList<>();
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

    // atualiza a aba de selecao de assentos
    private void updateTabSeat(Class selectedClass) {
        JPanel panelSeat = new JPanel();
        panelSeat.setLayout(new BoxLayout(panelSeat, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("Assentos disponiveis para a classe: " + (selectedClass != null ? selectedClass.getClassName() : "Nenhuma classe selecionada"));
        panelSeat.add(label);

        if (selectedClass != null) {
            panelAssentos.removeAll();
            List<Integer> availableSeats = selectedClass.generateSeats();
            List<Integer> occupiedSeats = getOccupiedSeats(selectedClass.getId());
            availableSeats.removeAll(occupiedSeats);

            for (int seatId : availableSeats) {
                JButton btnAssento = new JButton(String.valueOf(seatId));
                btnAssento.addActionListener(e -> {
                    Seat seat = new Seat();
                    seat.setId_Seat(seatId);
                    seat.setPrice(selectedClass.getPrice());
                    seat.setSeatClass(selectedClass);
                    selectedSeats.add(seat);
                    marcarAssentoComoOcupado(seatId);
                    btnAssento.setEnabled(false);
                    btnAssento.setBackground(Color.ORANGE);
                    JOptionPane.showMessageDialog(this, "Assento selecionado: " + seatId, "Informacao", JOptionPane.INFORMATION_MESSAGE);
                });
                panelAssentos.add(btnAssento);
            }

            JScrollPane scrollPane = new JScrollPane(panelAssentos);
            panelSeat.add(scrollPane);

            JButton btnNext = new JButton("Proximo");
            btnNext.addActionListener(e -> {
                if (selectedSeats.size() < numberOfPassengers) {
                    JOptionPane.showMessageDialog(this, "Selecione um assento para cada passageiro!", "Erro", JOptionPane.ERROR_MESSAGE);
                } else {
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

    // obtem os assentos ocupados
    private List<Integer> getOccupiedSeats(int classId) {
        List<Integer> occupiedSeats = new ArrayList<>();
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

    // marca um assento como ocupado
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

    // carrega os assentos disponiveis
    private void carregarAssentosDisponiveis(int classId) {
        panelAssentos.removeAll();
        List<Integer> allSeats = selectedClass.generateSeats();
        List<Integer> occupiedSeats = getOccupiedSeats(classId);

        // filtra assentos ocupados
        allSeats.removeAll(occupiedSeats);

        for (int seatId : allSeats) {
            JButton btnAssento = new JButton(String.valueOf(seatId));
            btnAssento.addActionListener(e -> {
                Seat seat = new Seat();
                seat.setId_Seat(seatId);
                seat.setPrice(selectedClass.getPrice());
                seat.setSeatClass(selectedClass);
                selectedSeats.add(seat);
                marcarAssentoComoOcupado(seatId);
                btnAssento.setEnabled(false);
                btnAssento.setBackground(Color.ORANGE);
                JOptionPane.showMessageDialog(this, "Assento selecionado: " + seatId, "Informacao", JOptionPane.INFORMATION_MESSAGE);
            });
            panelAssentos.add(btnAssento);
        }

        panelAssentos.revalidate();
        panelAssentos.repaint();
    }

    // cria a aba de finalizacao
    private void tabFinalize() {
        JPanel panelFinalize = new JPanel(new BorderLayout());
        JButton btnFinalize = new JButton("Finalizar Compra");

        btnFinalize.addActionListener(e -> {
            boolean refundable = true;
            int idFlight = selectedFlight.getId_Flight();

            for (int i = 0; i < passengers.size(); i++) {
                Passenger passenger = passengers.get(i);
                Seat seat = selectedSeats.get(i);

                // gera um ID unico para cada bilhete
                int ticketId = new Random().nextInt(1000000);

                // salva o bilhete no banco de dados
                Main.SaveTicket(String.valueOf(ticketId), passenger.getId_Passenger(), passenger.getName(), seat.getId_Seat(), selectedDestination, selectedClass.getPrice(), selectedSource, refundable, idFlight);

                // salva o assento no banco de dados
                Main.saveSeatInfo(String.valueOf(ticketId), passenger.getName(), seat.getId_Seat(), seat.getPrice(), true, seat.getSeatClass(), idFlight);

                // gera o PDF do bilhete
                String fileName = "Bilhete_" + passenger.getName() + "_" + ticketId + ".pdf";
                PDFGenerator.generateTicketPDF(
                        fileName,
                        selectedSource,
                        selectedDestination,
                        selectedFlight.getCodeName(),
                        passenger.getName(),
                        String.valueOf(seat.getId_Seat()),
                        seat.getPrice()
                );
            }

            JOptionPane.showMessageDialog(this, "Compra Finalizada com Sucesso! PDFs gerados.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            tabbedPane.setSelectedIndex(0);
        });

        panelFinalize.add(btnFinalize, BorderLayout.CENTER);
        tabbedPane.addTab("Finalizar", panelFinalize);
    }

    public static void main(String[] args) {
        new CompraBilhete();
    }
}