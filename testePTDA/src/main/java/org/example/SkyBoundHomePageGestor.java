package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;


public class SkyBoundHomePageGestor {
    private JPanel panel1;
    private JButton addFlightsButton;
    private JButton viewFlightsButton;
    private JButton manageClassesButton;

    public SkyBoundHomePageGestor() {
        JFrame frame = new JFrame("SkyBound Home");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setVisible(true);

        addFlightsButton.addActionListener(e -> new SkyBoundGestaoVoos());
        viewFlightsButton.addActionListener(e -> mostrarVoos());
        manageClassesButton.addActionListener(e -> abrirGestaoServicos());
    }

    static void mostrarVoos() {
        JFrame frame = new JFrame("Voos Cadastrados");
        /*DefaultListModel<String> model = new DefaultListModel<>();
        for (Flight flight : Flight.getFlights()) {
            model.addElement(flight.toString());
        }


        JList<String> list = new JList<>(model);
        frame.add(new JScrollPane(list), BorderLayout.CENTER);
        frame.setSize(400, 300);

        JList<String> list = new JList<>(model);*/
        String[] columnNames = {"ID Avião", "ID Voo", "Máx. Passageiros","Data", "Hora Partida", "Hora Chegada", "Destino", "Origem", "Código"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
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
                model.addRow(row);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setSize(500, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    static void abrirGestaoServicos() {
        new GestaoServicosClasses();
    }

    public static void main(String[] args) {
        new SkyBoundHomePageGestor();
    }
}
