package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.*;
import java.text.ParseException;

public class CompraBilhete extends JFrame {
    private final JTabbedPane tabbedPane;
    private final CheckIn checkInData = new CheckIn();
    JPanel panelDestinoOrigemData;
    JPanel panelSeat;
    private String selectedSource;
    private String selectedDestination;
    private Flight selectedFlight;
    private JTable tableFlights;
    private Passenger passenger;
    private Class selectedClass;
    private Class luxurious,economical;
    private ArrayList<Class> classes = new ArrayList<>();


    public CompraBilhete() {
        setTitle("Compra de Bilhete");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();

        tabDestinationSource();
        tabHourFlight();
        tabClassService();
        tabSeat
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

    private ArrayList<Flight> filterFlights(String origem, String destino) {
        ArrayList<Flight> filteredFlights = new ArrayList<>();
        for (Flight flight : getAvailableFlights()) {
            if (flight.getSource().equals(origem) && flight.getDestination().equals(destino)) {
                filteredFlights.add(flight);
            }
        }
        return filteredFlights;
    }

    private ArrayList<Flight> getAvailableFlights() {
        return Flight.getFlights();

    }

    private void updateFlights() {
        DefaultTableModel model = (DefaultTableModel) tableFlights.getModel();
        model.setRowCount(0);

        ArrayList<Flight> filteredFlights = filterFlights(selectedSource, selectedDestination);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        for (Flight flight : filteredFlights) {
            model.addRow(new Object[]{sdf.format(flight.gethTakeoff()), flight.getSource(), flight.getDestination()});
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
       /* String[] columns = {"Hora", "Origem", "Destino"};
        Object[][] flights = {
                {"10:00", "Lisboa", "Porto"},
                {"12:30", "Lisboa", "Madrid"},
                {"14:00", "Porto", "Londres"},
                {"16:30", "Madrid", "Paris"},
                {"18:00", "Londres", "Lisboa"}
        }; */

        tableFlights = new JTable(new DefaultTableModel(new Object[]{"Hora", "Origem", "Destino", "Preço"}, 0));
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
            selectedClass = (Class) comboClass.getSelectedItem();
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
        ArrayList<String> services1 = new ArrayList<>();
        services1.add("Bagagem Extra");
        services1.add("Refeição Gourmet");

        ArrayList<String> services2 = new ArrayList<>();
        services2.add("Embarque Prioritário");

        classes = new ArrayList<>();
        luxurious = new Class("Luxuosa", 200.00, 10, services1);
        classes.add(luxurious);
        economical = new Class("Económica", 100.00, 50, services2);
        classes.add(economical);

        return classes;
    }
    private void tabSeat(){

        if (selectedClass.equals(luxurious)) {
            JLabel label = new JLabel("Você escolheu a classe Luxuosa.");
            ClasseN2 panelSeat2 = new ClasseN2();
            panelSeat2.add(label, BorderLayout.CENTER);
            panelSeat = panelSeat2.getPanel();

        } else if (selectedClass.equals(economical)) {
            JLabel label = new JLabel("Você escolheu a classe Económica.");
            ClasseN3 panelSeat3 = new ClasseN3();
            panelSeat3.add(label, BorderLayout.CENTER);
            panelSeat = panelSeat3.getPanel();
        }

        JButton btnNext = new JButton("Próximo");
        btnNext.addActionListener(e -> {
            panelSeat.add(btnNext, BorderLayout.CENTER);
            tabbedPane.setSelectedIndex(4);
        });

        tabbedPane.addTab("Assento", panelSeat);
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
                tabbedPane.setSelectedIndex(4);
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
            Ticket ticket = new Ticket("Lisboa", "Porto", 123456, 150.00);
            boolean refundable = true; // ou false conforme necessário
            int idTicket = new Random().nextInt(1000000);
            Main.SaveTicket(passenger.getId_Passenger(), selectedDestination,ticket.getPrice(), selectedSource, refundable, idTicket);
            Main.saveSeatInfo(idTicket, seat, ticket.getPrice(),selectedClass);
            JOptionPane.showMessageDialog(this, "Bilhete Criado:\n" + ticket.toString(), "Bilhete", JOptionPane.INFORMATION_MESSAGE);
            tabbedPane.setSelectedIndex(0);
        });
        panelFinalize.add(btnFinalize, BorderLayout.CENTER);

        tabbedPane.addTab("Finalizar", panelFinalize);
    }

    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            Flight.addFlight(33, 200, 255, sdf.parse("10:30"),sdf.parse("11:45"),"Porto","Lisboa","LisboaPorto");
            Flight.addFlight(34, 301, 280, sdf.parse("12:30"),sdf.parse("14:15"),"Londres","Lisboa","LisboaMadrid");
            Flight.addFlight(780, 999, 275, sdf.parse("14:00"),sdf.parse("16:50"),"Londres","Porto","PortoLondres");
            Flight.addFlight(96024, 246, 300, sdf.parse("16:30"), sdf.parse("18:10"),"Paris","Madrid","MadridParis");
            Flight.addFlight(123456, 678, 260, sdf.parse("18:00"),sdf.parse("19:30"),"Lisboa","Londres","LondresLisboa");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        new CompraBilhete();
    }
}
