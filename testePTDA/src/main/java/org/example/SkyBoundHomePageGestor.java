package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class SkyBoundHomePageGestor {
    private JPanel panel1; // painel principal da interface
    private JButton addFlightsButton; // botao para adicionar voos
    private JButton viewFlightsButton; // botao para visualizar voos
    private JButton manageClassesButton; // botao para gerir classes e servicos
    private JTable table; // tabela para exibir os voos

    // construtor da classe
    public SkyBoundHomePageGestor() {
        JFrame frame = new JFrame("SkyBound Home"); // cria a janela principal
        frame.setContentPane(panel1); // define o painel principal
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // fecha a aplicacao ao fechar a janela
        frame.setSize(600, 400); // define o tamanho da janela
        frame.setVisible(true); // torna a janela visivel

        // adiciona um listener ao botao de adicionar voos
        addFlightsButton.addActionListener(e -> new SkyBoundGestaoVoos());

        // adiciona um listener ao botao de visualizar voos
        viewFlightsButton.addActionListener(e -> mostrarVoos());

        // adiciona um listener ao botao de gerir classes e servicos
        manageClassesButton.addActionListener(e -> abrirGestaoServicos());
    }

    // metodo para mostrar a lista de voos
    static void mostrarVoos() {
        JFrame frame = new JFrame("Lista de Voos"); // cria uma nova janela para a lista de voos

        // define as colunas da tabela
        String[] columnNames = {"ID Aviao", "ID Voo", "Max. Passageiros", "Data", "Hora Partida", "Hora Chegada", "Destino", "Origem", "Codigo"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // torna as celulas da tabela nao editaveis
            }
        };

        JTable table = new JTable(model); // cria a tabela com o modelo definido
        JScrollPane scrollPane = new JScrollPane(table); // adiciona a tabela a um painel de scroll
        frame.add(scrollPane, BorderLayout.CENTER); // adiciona o painel de scroll a janela
        frame.setSize(500, 600); // define o tamanho da janela
        frame.setLocationRelativeTo(null); // centraliza a janela
        frame.setVisible(true); // torna a janela visivel

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")) {
            String sql = "SELECT * FROM flight"; // consulta SQL para obter todos os voos
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            // percorre o resultado da consulta e adiciona os dados a tabela
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
                model.addRow(row); // adiciona uma linha a tabela
            }

        } catch (SQLException e) {
            throw new RuntimeException(e); // trata excecoes de SQL
        }

        // adiciona um listener de clique duplo a tabela
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) { // verifica se foi um clique duplo
                    int selectedRow = table.getSelectedRow(); // obtem a linha selecionada
                    if (selectedRow != -1) { // verifica se uma linha foi selecionada
                        abrirGestaoTripulacao(table, selectedRow); // abre a gestao de tripulacao
                    }
                }
            }
        });
    }

    // metodo para abrir a gestao de tripulacao
    private static void abrirGestaoTripulacao(JTable table, int selectedRow) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")) {
            // obtem o ID do voo da linha selecionada
            int idFlight = (int) table.getValueAt(selectedRow, 1);

            // consulta o voo correspondente ao ID
            String sql = "SELECT * FROM flight WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idFlight);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // cria um objeto Flight com os dados do voo
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

                // abre a tela de gestao de tripulacao
                new GestaoTripulacao(selectedFlight);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao abrir gestao de tripulacao: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // metodo para abrir a gestao de servicos e classes
    static void abrirGestaoServicos() {
        new GestaoServicosClasses();
    }

    // metodo principal
    public static void main(String[] args) {
        new SkyBoundHomePageGestor();
    }
}