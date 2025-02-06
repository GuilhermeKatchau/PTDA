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

    // Construtor da classe SkyBoundGestaoVoos
    public SkyBoundGestaoVoos() {

        setTitle("Gestao de Voos");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Inicializacao dos campos de texto
        id_Airplane = new JTextField();
        id_Flight = new JTextField();
        codeName = new JTextField();
        source = new JTextField();
        destination = new JTextField();

        // Configuracao do campo de texto formatado para numeros inteiros
        NumberFormat numberFormat = NumberFormat.getIntegerInstance();
        maxPassengers = new JFormattedTextField(numberFormat);
        maxPassengers.setValue(0);

        // Configuracao dos spinners para datas e horas
        date1 = new JSpinner(new SpinnerDateModel());
        date1.setEditor(new JSpinner.DateEditor(date1, "dd/MM/yyyy"));

        hTakeOff = new JSpinner(new SpinnerDateModel());
        hTakeOff.setEditor(new JSpinner.DateEditor(hTakeOff, "dd/MM/yyyy HH:mm"));

        hLanding = new JSpinner(new SpinnerDateModel());
        hLanding.setEditor(new JSpinner.DateEditor(hLanding, "dd/MM/yyyy HH:mm"));

        // Inicializacao da lista de voos registrados
        flights = new DefaultListModel<>();
        registeredFlights = new JList<>(flights);

        // Criacao do painel de formulario e da lista de voos
        JPanel formPanel = Form();
        JScrollPane scrollPane = FlightList(); // Atualizado para carregar os dados da base de dados
        add(scrollPane, BorderLayout.CENTER);

        // Criacao do painel de botoes
        JPanel buttonPanel = Buttons();

        // Adiciona os paineis ao frame
        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Carrega os voos da base de dados e torna o frame visivel
        loadFlights();
        setVisible(true);
    }

    // Metodo para criar o painel de formulario
    private JPanel Form() {
        JPanel formPanel = new JPanel(new GridLayout(5, 4, 10, 10));

        // Adiciona os campos de texto e labels ao painel de formulario
        formPanel.add(new JLabel("ID do Aviao:"));
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

    // Metodo para criar a lista de voos
    private JScrollPane FlightList() {
        // Configura o modelo da tabela para exibir os voos
        String[] columnNames = {"ID Aviao", "ID Voo", "Max. Passageiros", "Data", "Hora Partida", "Hora Chegada", "Destino", "Origem", "Codigo"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Impede a edicao de qualquer celula
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

        // Adiciona um listener para abrir a gestao de tripulacao ao clicar duas vezes na linha
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) { // Clique duplo
                    int selectedRow = table.getSelectedRow(); // Obtem a linha selecionada
                    abrirGestaoTripulacao(selectedRow);
                }
            }
        });

        // Configura o JScrollPane para exibir a tabela
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Voos Cadastrados"));

        return scrollPane;
    }

    // Metodo para criar o painel de botoes
    private JPanel Buttons() {
        JPanel buttonPanel = new JPanel(new FlowLayout());

        // Botao para adicionar voo
        JButton btnAdicionar = new JButton("Adicionar Voo");
        btnAdicionar.addActionListener(e -> addFlight());

        // Botao para remover voo
        JButton btnRemover = new JButton("Remover Voo");
        btnRemover.addActionListener(e -> removeFlight());

        // Adiciona os botoes ao painel
        buttonPanel.add(btnAdicionar);
        buttonPanel.add(btnRemover);

        return buttonPanel;
    }

    // Metodo para adicionar um voo
    private void addFlight() {
        try {
            // Verifica se ID do Aviao e ID do Voo sao numericos
            int id_Airplane = Integer.parseInt(this.id_Airplane.getText());
            int id_Flight = Integer.parseInt(this.id_Flight.getText());
            String codename = codeName.getText();
            String source = this.source.getText();
            String destination = this.destination.getText();
            int maxPassengers = Integer.parseInt(this.maxPassengers.getText());
            Date date1 = (Date) this.date1.getValue();
            Date hTakeOff = (Date) this.hTakeOff.getValue();
            Date hLanding = (Date) this.hLanding.getValue();

            // Adiciona o voo a base de dados e atualiza a lista de voos
            Flight.addFlight(id_Airplane, id_Flight, maxPassengers, date1, hTakeOff, hLanding, destination, source, codename);
            Main.salvarDadosFlight(id_Airplane, id_Flight, maxPassengers, date1, hTakeOff, hLanding, destination, source, codename);
            loadFlights();
            clearFields();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Os campos ID do Aviao, ID do Voo devem ser numericos!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Metodo para remover um voo
    private void removeFlight() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow != -1) {
            int flightId = (int) table.getValueAt(selectedRow, 1);

            try (Connection conn = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")) {
                // Query para remover o voo da base de dados
                String sql = "DELETE FROM flight WHERE id_flight = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, flightId); // Define o ID do voo a ser removido

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Voo removido com sucesso da base de dados!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Voo nao encontrado na base de dados!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao remover voo da base de dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }

            // Remove o voo da lista de voos em memoria
            Flight.removeFlight(flightId); // Chamando o metodo de remocao na classe Flight

            // Atualiza a tabela apos a remocao
            loadFlights();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um voo para remover!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Metodo para carregar os voos da base de dados
    private void loadFlights() {
        flights.clear();
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", // URL do BD
                "PTDA24_05",                                       // Usuario
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
                flights.addElement("ID Aviao: " + idAirplane
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

    // Metodo para abrir a gestao de tripulacao
    private void abrirGestaoTripulacao(int selectedRow) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")) {
            String sql = "SELECT * FROM flight LIMIT ?, 1"; // Obtem o voo correspondente a linha selecionada
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
            JOptionPane.showMessageDialog(this, "Erro ao abrir gestao de tripulacao: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Metodo para limpar os campos do formulario
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

    // Metodo principal para iniciar a aplicacao
    public static void main(String[] args) {
        new SkyBoundGestaoVoos();
    }
}