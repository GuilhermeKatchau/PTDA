package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SkyBoundGestaoVoos extends JFrame {

    private final JTextField id_Airplane, id_Flight, codeName, source, destination;
    private final JFormattedTextField maxPassengers;
    private final JSpinner hTakeOff, hLanding, date1;
    final DefaultListModel<String> flights;
    private final JList<String> registeredFlights;
    private JTable table;

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

        date1 = new JSpinner(new SpinnerDateModel());
        date1.setEditor(new JSpinner.DateEditor(date1, "dd/MM/yyyy"));

        hTakeOff = new JSpinner(new SpinnerDateModel());
        hTakeOff.setEditor(new JSpinner.DateEditor(hTakeOff, "dd/MM/yyyy HH:mm"));

        hLanding = new JSpinner(new SpinnerDateModel());
        hLanding.setEditor(new JSpinner.DateEditor(hLanding, "dd/MM/yyyy HH:mm"));

        flights = new DefaultListModel<>();
        registeredFlights = new JList<>(flights);

        JPanel formPanel = Form();
        JScrollPane scrollPane = FlightList(); // Atualizado para carregar os dados da base de dados
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = Buttons();
        /*
        registeredFlights.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    abrirGestaoTripulacao();
                }
            }
        }); */

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

        formPanel.add(new JLabel("Data:"));
        formPanel.add(date1);

        formPanel.add(new JLabel("Tempo de Partida:"));
        formPanel.add(hTakeOff);

        formPanel.add(new JLabel("Tempo de Chegada:"));
        formPanel.add(hLanding);

        return formPanel;
    }

    private JScrollPane FlightList() {
        // Configura o modelo da tabela para exibir os voos
        String[] columnNames = {"ID Avião", "ID Voo", "Máx. Passageiros", "Data", "Hora Partida", "Hora Chegada", "Destino", "Origem", "Código"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Impede a edição de qualquer célula
            }
        };
        table = new JTable(model);

        // Carrega os dados da base de dados no modelo da tabela
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")) {
            String sql = "SELECT * FROM flight";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id_plane"),
                        rs.getInt("id"),
                        rs.getInt("maxPassengers"),
                        rs.getString("date1"),
                        rs.getString("timeTakeOff"),
                        rs.getString("timeLanding"),
                        rs.getString("destination"),
                        rs.getString("source1"),
                        rs.getString("codename")
                };
                model.addRow(row); // Adiciona cada linha de dados ao modelo
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar voos da base de dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) { // Clique único
                    int selectedRow = table.getSelectedRow(); // Obtém a linha selecionada
                        abrirGestaoTripulacao(selectedRow);
                }
            }
        });

        // Configura o JScrollPane para exibir a tabela
        JScrollPane scrollPane = new JScrollPane(table);
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
            // Verifica se ID do Avião e ID do Voo são numéricos
            int id_Airplane = Integer.parseInt(this.id_Airplane.getText());
            int id_Flight = Integer.parseInt(this.id_Flight.getText());
            String codename = codeName.getText();
            String source = this.source.getText();
            String destination = this.destination.getText();
            int maxPassengers = Integer.parseInt(this.maxPassengers.getText());
            Date date1 = (Date) this.date1.getValue();
            Date hTakeOff = (Date) this.hTakeOff.getValue();
            Date hLanding = (Date) this.hLanding.getValue();

            Flight.addFlight(id_Flight, maxPassengers, date1, hTakeOff, hLanding, destination, source, codename);
            Main.salvarDadosFlight(id_Airplane, id_Flight, maxPassengers,date1, hTakeOff, hLanding, destination, source, codename);
            loadFlights();
            clearFields();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Os campos ID do Avião, ID do Voo devem ser numéricos!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeFlight() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow != -1) {
            int flightId = (int) table.getValueAt(selectedRow, 1);

            // Remove o voo da base de dados usando o ID do voo
            try (Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")) {
                String sql = "DELETE FROM flight WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, flightId);
                stmt.executeUpdate();
                loadFlights(); // Atualiza a tabela após a remoção
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao remover voo: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um voo para remover!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadFlights() {
        flights.clear();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", // URL do BD
                "PTDA24_05",                                       // Usuário
                "Potm%793"                                         // Senha
        )) {
            // Consulta para buscar os voos
            String sql = "SELECT id_plane, id, maxPassengers, date1, timeTakeOff, timeLanding, destination, source1, codename FROM flight";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            // Itera pelos resultados da consulta
            while (rs.next()) {
                int idAirplane = rs.getInt("id_plane");
                int idFlight = rs.getInt("id");
                int maxPassengers = rs.getInt("maxPassengers");
                String date = rs.getString("date1");
                String takeoff = rs.getString("timeTakeOff");
                String landing = rs.getString("timeLanding");
                String destination = rs.getString("destination");
                String source = rs.getString("source1");
                String codename = rs.getString("codename");

                // Adiciona os dados ao modelo flights
                flights.addElement("ID Avião: " + idAirplane
                        + " | ID Voo: " + idFlight
                        + " | Code Name: " + codename
                        + " | Origem: " + source
                        + " | Destino: " + destination
                        + " | Partida: " + date + " " + takeoff
                        + " | Chegada: " + date + " " + landing
                        + " | Limite: " + maxPassengers);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar voos da base de dados: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirGestaoTripulacao(int selectedRow) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")) {
            String sql = "SELECT * FROM flight LIMIT ?, 1"; // Obtém o voo correspondente à linha selecionada
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, selectedRow);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Flight selectedFlight = new Flight(

                        rs.getInt("id_plane"),
                        rs.getInt("id"),
                        rs.getInt("maxPassengers"),
                        rs.getDate("date1"),
                        rs.getTimestamp("timeTakeOff"),
                        rs.getTimestamp("timeLanding"),
                        rs.getString("destination"),
                        rs.getString("source1"),
                        rs.getString("codename")
                );
                new GestaoTripulacao(selectedFlight);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao abrir gestão de tripulação: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
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
