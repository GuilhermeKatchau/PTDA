package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;
import java.text.ParseException;

public class CompraBilhete extends JFrame {
    private final JTabbedPane tabbedPane;
    private final CheckIn checkInData = new CheckIn();
    JPanel panelDestinoOrigemData;
    private String selectedSource;
    private String selectedDestination;
    private Flight selectedFlight;
    JTable tableFlights;
    private Passenger passenger;
    Class selectedClass, luxurious, economical, premium;
    private ArrayList<Class> classes = new ArrayList<>();
    private int idTicket = new Random().nextInt(1000000);
    private Ticket ticket;

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

    public void setAvailableFlights(ArrayList<Flight> flights) {
        ArrayList<Flight> availableFlights = flights;
    }

    private void updateFlights() {
        DefaultTableModel model = (DefaultTableModel) tableFlights.getModel();
        model.setRowCount(0); // Limpa os dados existentes na tabela

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")) {
            // Query para filtrar voos com base na origem e destino
            String sql = "SELECT date1, timeTakeOff, source1, destination FROM flight WHERE source1 = ? AND destination = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, selectedSource); // Define o valor para origem
            stmt.setString(2, selectedDestination); // Define o valor para destino

            ResultSet rs = stmt.executeQuery();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

            while (rs.next()) {
                Object[] row = {
                        rs.getString("date1"),
                        rs.getString("timeTakeOff"),
                        rs.getString("source1"),
                        rs.getString("destination")
                };
                model.addRow(row); // Adiciona a linha à tabela
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar voos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }


    private Flight getFlightFromRow(int row) {
        DefaultTableModel model = (DefaultTableModel) tableFlights.getModel();

        String hora = model.getValueAt(row, 0).toString();
        String origem = model.getValueAt(row, 1).toString();
        String destino = model.getValueAt(row, 2).toString();

        for (Flight flight : filterFlights(selectedSource, selectedDestination)) {
            if (flight.getSource().equals(origem) && flight.getDestination().equals(destino)) {
                return flight;
            }
        }
        return null;
    }

    private void tabHourFlight() {
        JPanel panelHourFlight = new JPanel(new BorderLayout());

        String[] columns = {"Data","Hora", "Origem", "Destino"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        tableFlights = new JTable(model);

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")) {
            String sql = "SELECT date1, timeTakeOff, source1, destination FROM flight WHERE source1 = ? AND destination = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, selectedSource); // Define a origem escolhida pelo passageiro
            stmt.setString(2, selectedDestination); // Define o destino escolhido pelo passageiro
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                        rs.getString("date1"),
                        rs.getString("timeTakeOff"),
                        rs.getString("source1"),
                        rs.getString("destination")
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


    private void tabClassService() {
        JPanel panelClassService = new JPanel(new GridLayout(6, 1, 10, 10));
        JLabel labelClass = new JLabel("Escolha a Classe:");

        ArrayList<Class> availableClasses = getAvailableClasses(); // Método que retorna classes disponíveis
        JComboBox<Class> comboClass = new JComboBox<>(availableClasses.toArray(new Class[0]));
        selectedClass = (Class) comboClass.getSelectedItem();

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
            }
            panelServices.revalidate();
            panelServices.repaint();
        });

        btnNext.addActionListener(e -> {
            if (selectedClass == null) {
                JOptionPane.showMessageDialog(tabbedPane, "Por favor, selecione uma classe!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            StringBuilder selectedServices = new StringBuilder();

            // Coleta os serviços selecionados
            for (Component component : panelServices.getComponents()) {
                if (component instanceof JCheckBox && ((JCheckBox) component).isSelected()) {
                    selectedServices.append(((JCheckBox) component).getText()).append(", ");
                }
            }

            String services = selectedServices.toString().replaceAll(", $", "");
            JOptionPane.showMessageDialog(this, "Classe Selecionada: " + selectedClass +
                    "\nServiços Adicionais: " + (services.isEmpty() ? "Nenhum" : services), "Resumo - Classe e Serviços", JOptionPane.INFORMATION_MESSAGE);

            updateTabSeat();
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
            classes = new ArrayList<>();
            ArrayList<String> services1 = new ArrayList<>();
            services1.add("Bagagem Extra");
            services1.add("Refeição Gourmet");

            ArrayList<String> services2 = new ArrayList<>();
            services2.add("Embarque Prioritário");

            ArrayList<String> services3 = new ArrayList<>();
            services3.add("");
            services3.add("");
            services3.add("");

            luxurious = new Class("Luxuosa", 200.00, 16, services1);
            economical = new Class("Económica", 100.00, 64, services2);
            premium = new Class("Premium",150.00,24,services3);

            classes.add(economical);
            classes.add(luxurious);
            classes.add(premium);
        return classes;
    }
    private void tabSeat() {

        JPanel panelSeatPlaceholder = new JPanel();
        JLabel placeholderLabel = new JLabel("Por favor, selecione uma classe primeiro.");
        panelSeatPlaceholder.add(placeholderLabel);

        tabbedPane.addTab("Assento", panelSeatPlaceholder);
    }


    private int selectedSeatId = 0;
    private double selectedSeatPrice = 0.0;
    private Class selectedSeatClass = null;
    Seat selectedSeat = new Seat();

    private void updateTabSeat() {
        JPanel panelSeat = new JPanel();
        panelSeat.setLayout(new BoxLayout(panelSeat, BoxLayout.Y_AXIS)); // Define o layout como BoxLayout

        if (selectedClass.equals(luxurious)) {
            JLabel label = new JLabel("Você escolheu a classe Luxuosa.");
            ClasseN3 panelSeat3 = new ClasseN3();
            setSize(700, 600);
            panelSeat.add(label);
            panelSeat.add(panelSeat3.getPanel());

            JButton[] botoesAssentos = panelSeat3.getBotoesAssentos();
            for (int i = 0; i < botoesAssentos.length; i++) {
                int numeroAssento = i + 1;
                int finalI = i;
                botoesAssentos[i].addActionListener(e -> {
                    // Atualizar informações do assento selecionado
                    selectedSeat.setId_Seat(numeroAssento);
                    selectedSeat.setPrice(calcularPreco(numeroAssento));
                    selectedSeatClass = luxurious;

                    JOptionPane.showMessageDialog(
                            tabbedPane,
                            "Assento selecionado: " + selectedSeat.getId_Seat() +
                                    "\nPreço: " + selectedSeat.getPrice()
                    );
                    botoesAssentos[finalI].setBackground(Color.orange);
                });
            }

            JButton btnNext = new JButton("Próximo");
            btnNext.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            btnNext.setAlignmentX(JButton.CENTER_ALIGNMENT);
            btnNext.addActionListener(e -> {
                if (selectedSeat.getId_Seat() == 0) {
                    JOptionPane.showMessageDialog(tabbedPane, "Por favor, selecione um assento antes de continuar!", "Erro", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(tabbedPane, "Assento selecionado com sucesso!", "Informação", JOptionPane.INFORMATION_MESSAGE);
                    tabbedPane.setSelectedIndex(4);
                }
            });

            panelSeat.add(Box.createRigidArea(new Dimension(0, 10))); // Espaçamento entre assentos e botão
            panelSeat.add(btnNext);

        } else if (selectedClass.equals(economical)) {
            JLabel label = new JLabel("Você escolheu a classe Económica.");
            SkyBoundAdicionarAssento panelSeat2 = new SkyBoundAdicionarAssento();
            JPanel economicalPanel = new JPanel();
            economicalPanel.setLayout(new BoxLayout(economicalPanel, BoxLayout.Y_AXIS));

            economicalPanel.add(label);
            economicalPanel.add(panelSeat2.getPanel());

            JButton[] botoesAssentos = panelSeat2.getBotoesAssentos();
            for (int i = 0; i < botoesAssentos.length; i++) {
                int numeroAssento = i + 1;
                botoesAssentos[i].addActionListener(e -> {
                    // Atualizar informações do assento selecionado
                    selectedSeat.setId_Seat(numeroAssento);
                    selectedSeat.setPrice(calcularPreco(numeroAssento));
                    selectedSeatClass = economical;

                    JOptionPane.showMessageDialog(
                            tabbedPane,
                            "Assento selecionado: " + selectedSeatId +
                                    "\nPreço: " + selectedSeatPrice
                    );
                });
            }

            JButton btnNext = new JButton("Próximo");
            btnNext.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            btnNext.setAlignmentX(JButton.CENTER_ALIGNMENT);
            btnNext.addActionListener(e -> {
                if (selectedSeat.getId_Seat() == 0) {
                    JOptionPane.showMessageDialog(tabbedPane, "Por favor, selecione um assento antes de continuar!", "Erro", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(tabbedPane, "Assento selecionado com sucesso!", "Informação", JOptionPane.INFORMATION_MESSAGE);
                    tabbedPane.setSelectedIndex(4);
                }
            });

            economicalPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Espaçamento entre assentos e botão
            economicalPanel.add(btnNext);

            JScrollPane scrollPane = new JScrollPane(economicalPanel);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

            panelSeat.add(scrollPane);
        } if (selectedClass.equals(premium)) {
            JLabel label = new JLabel("Você escolheu a classe Premium.");
            ClasseN2 panelSeat2 = new ClasseN2();
            setSize(700, 600);
            panelSeat.add(label);
            panelSeat.add(panelSeat2.getPanel());

            JButton[] botoesAssentos = panelSeat2.getBotoesAssentos();
            for (int i = 0; i < botoesAssentos.length; i++) {
                int numeroAssento = i + 1;
                int finalI = i;
                botoesAssentos[i].addActionListener(e -> {
                    // Atualizar informações do assento selecionado
                    selectedSeat.setId_Seat(numeroAssento);
                    selectedSeat.setPrice(calcularPreco(numeroAssento));
                    selectedSeatClass = premium;

                    JOptionPane.showMessageDialog(
                            tabbedPane,
                            "Assento selecionado: " + selectedSeat.getId_Seat() +
                                    "\nPreço: " + selectedSeat.getPrice()
                    );
                    botoesAssentos[finalI].setBackground(Color.orange);
                });
            }

            JButton btnNext = new JButton("Próximo");
            btnNext.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            btnNext.setAlignmentX(JButton.CENTER_ALIGNMENT);
            btnNext.addActionListener(e -> {
                if (selectedSeat.getId_Seat() == 0) {
                    JOptionPane.showMessageDialog(tabbedPane, "Por favor, selecione um assento antes de continuar!", "Erro", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(tabbedPane, "Assento selecionado com sucesso!", "Informação", JOptionPane.INFORMATION_MESSAGE);
                    tabbedPane.setSelectedIndex(4);
                }
            });
            panelSeat.add(Box.createRigidArea(new Dimension(0, 10))); // Espaçamento entre assentos e botão
            panelSeat.add(btnNext);
        }

        int index1 = tabbedPane.indexOfTab("Assento");
        tabbedPane.setComponentAt(index1, panelSeat);

        tabbedPane.revalidate();
        tabbedPane.repaint();
    }



    double calcularPreco(double price) {
        // Lógica para calcular preço com base no número do assento
        if (selectedClass.equals(economical)) {
            price = 100.00;
        } else if (selectedClass.equals(premium)) {
            price = 150.00;
        } else if (selectedClass.equals(luxurious)) {
            price = 200.00;
        }
        return price;
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
                int id = new Random().nextInt(1000000); // Gerando um ID aleatório para o passageiro
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

            // Salvar o ticket no banco de dados
            Main.SaveTicket(passenger.getId_Passenger(), selectedDestination, ticket.getPrice(), selectedSource, refundable, idTicket);

            // Salvar informações do assento no banco de dados
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
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        new CompraBilhete();
    }
}