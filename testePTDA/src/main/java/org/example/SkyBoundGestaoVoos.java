package org.example;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SkyBoundGestaoVoos extends JFrame {

    private final JTextField id_Airplane, id_Flight, codeName, source, destination;
    private final JFormattedTextField maxPassengers;
    private final JSpinner hTakeOff, hLanding;
    private final DefaultListModel<String> flights;
    private final JList<String> registeredFlights;

    public SkyBoundGestaoVoos() {
        setTitle("Gestão de Voos");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        id_Airplane = new JTextField();
        id_Flight = new JTextField();

        codeName = new JTextField();
        source = new JTextField();
        destination = new JTextField();

        NumberFormat numberFormat = NumberFormat.getIntegerInstance();
        maxPassengers = new JFormattedTextField(numberFormat);
        maxPassengers.setValue(0);

        hTakeOff = new JSpinner(new SpinnerDateModel());
        hTakeOff.setEditor(new JSpinner.DateEditor(hTakeOff, "dd/MM/yyyy HH:mm"));

        hLanding = new JSpinner(new SpinnerDateModel());
        hLanding.setEditor(new JSpinner.DateEditor(hLanding, "dd/MM/yyyy HH:mm"));

        flights = new DefaultListModel<>();
        registeredFlights = new JList<>(flights);

        JPanel formPanel = Form();
        JScrollPane scrollPane = FlightList();
        JPanel buttonPanel = Buttons();

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadFlights();
        setVisible(true);
    }

    private JPanel Form() {
        JPanel formPanel = new JPanel(new GridLayout(5, 4, 10, 10));

        formPanel.add(new JLabel("ID do Avião:"));
        formPanel.add(id_Airplane);

        formPanel.add(new JLabel("ID do Voo:"));
        formPanel.add(id_Flight);

        formPanel.add(new JLabel("Code Name:"));
        formPanel.add(codeName);

        formPanel.add(new JLabel("Cidade de Origem:"));
        formPanel.add(source);

        formPanel.add(new JLabel("Cidade de Destino:"));
        formPanel.add(destination);

        formPanel.add(new JLabel("Limite de Passageiros:"));
        formPanel.add(maxPassengers);

        formPanel.add(new JLabel("Tempo de Partida:"));
        formPanel.add(hTakeOff);

        formPanel.add(new JLabel("Tempo de Chegada:"));
        formPanel.add(hLanding);

        return formPanel;
    }

    private JScrollPane FlightList() {
        JScrollPane scrollPane = new JScrollPane(registeredFlights);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Voos Cadastrados"));
        return scrollPane;
    }

    private JPanel Buttons() {
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton btnAdicionar = new JButton("Adicionar Voo");
        btnAdicionar.addActionListener(e -> addFlight());

        JButton btnRemover = new JButton("Remover Voo");
        btnRemover.addActionListener(e -> removeFlight());

        buttonPanel.add(btnAdicionar);
        buttonPanel.add(btnRemover);

        return buttonPanel;
    }

    private void addFlight() {
        try {
            // Verifica se todos os campos estão preenchidos


            // Verifica se ID do Avião e ID do Voo são numéricos
            int id_Airplane = Integer.parseInt(this.id_Airplane.getText());
            int id_Flight = Integer.parseInt(this.id_Flight.getText());
            String codename = codeName.getText();
            String source = this.source.getText();
            String destination = this.destination.getText();
            int maxPassengers = Integer.parseInt(this.maxPassengers.getText());
            Date hTakeOff = (Date) this.hTakeOff.getValue();
            Date hLanding = (Date) this.hLanding.getValue();

            Flight.addFlight(id_Airplane, id_Flight, maxPassengers, hTakeOff, hLanding, destination, source, codename);
            Main.salvarDadosFlight(id_Airplane, id_Flight, maxPassengers, hTakeOff, hLanding, destination, source, codename);
            loadFlights();
            clearFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Os campos ID do Avião, ID do Voo devem ser numéricos!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeFlight() {
        int selectedIndex = registeredFlights.getSelectedIndex();
        if (selectedIndex != -1) {
            Flight.removeFlight(selectedIndex);
            loadFlights();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um voo para remover!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadFlights() {
        flights.clear();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//TEM DE SE TROCAR PARA APARECER OS VOOS DA BD !!!!!
        for (Flight flight : Flight.getFlights()) {
            flights.addElement("ID Avião: " + flight.getId_Airplane()
                    + " | ID Voo: " + flight.getId_Flight()
                    + " | Code Name: " + flight.getCodeName()
                    + " | Origem: " + flight.getSource()
                    + " | Destino: " + flight.getDestination()
                    + " | Partida: " + dateFormat.format(flight.gethTakeoff())
                    + " | Chegada: " + dateFormat.format(flight.gethLanding())
                    + " | Limite: " + flight.getMaxPassengers());

        }
    }
    //Nao funciona, serve de exemplo
    private void abrirGestaoTripulacao() {
        int selectedIndex = registeredFlights.getSelectedIndex();
        if (selectedIndex != -1) {
            Flight selectedFlight = Flight.getFlights().get(selectedIndex);
            new GestaoTripulacao();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um voo para continuar!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        id_Airplane.setText("");
        id_Flight.setText("");
        codeName.setText("");
        source.setText("");
        destination.setText("");
        maxPassengers.setValue(0);
        hTakeOff.setValue(new Date());
        hLanding.setValue(new Date());
    }

    public static void main(String[] args) {
        new SkyBoundGestaoVoos();
    }
}
