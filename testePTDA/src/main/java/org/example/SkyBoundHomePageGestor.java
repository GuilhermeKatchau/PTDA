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
    private JTable table; // Adicionado para referência à tabela

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
        JFrame frame = new JFrame("Lista de Voos");

        String[] columnNames = {"ID Avião", "ID Voo", "Máx. Passageiros", "Data", "Hora Partida", "Hora Chegada", "Destino", "Origem", "Código"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model); // Tabela para exibir os voos
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setSize(500, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

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

        // Adiciona um listener de clique duplo à tabela
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) { // Clique duplo
                    int selectedRow = table.getSelectedRow(); // Obtém a linha selecionada
                    if (selectedRow != -1) { // Verifica se uma linha foi selecionada
                        abrirGestaoTripulacao(table, selectedRow); // Abre a gestão de tripulação
                    }
                }
            }
        });
    }

    // Método para abrir a gestão de tripulação
    private static void abrirGestaoTripulacao(JTable table, int selectedRow) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")) {
            // Obtém o ID do voo da linha selecionada
            int idFlight = (int) table.getValueAt(selectedRow, 1);

            // Consulta o voo correspondente ao ID
            String sql = "SELECT * FROM flight WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idFlight);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Cria um objeto Flight com os dados do voo
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

                // Abre a tela de gestão de tripulação
                new GestaoTripulacao(selectedFlight);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao abrir gestão de tripulação: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    static void abrirGestaoServicos() {
        new GestaoServicosClasses();
    }

    public static void main(String[] args) {
        new SkyBoundHomePageGestor();
    }
}