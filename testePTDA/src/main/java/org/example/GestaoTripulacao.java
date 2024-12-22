package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.sql.*;

public class GestaoTripulacao {
    private Flight flight;
    private JTextField idField, nameField, phoneField, shiftField, experienceField, rankField;
    private JLabel messageLabel;
    private final DefaultListModel<String> crewListModel;
    private ArrayList<Crew>  crewMembers = new ArrayList<>();
    private JList<String> crewListDisplay;
    private static final Color PRIMARY_COLOR = new Color(51, 153, 255);
    private static final Color SUCCESS_COLOR = new Color(75, 181, 67);
    private static final Color ERROR_COLOR = new Color(220, 53, 69);
    private static int id;

    public GestaoTripulacao(Flight flight) {
        this.flight = flight;
        crewListModel = new DefaultListModel<>();
        initializeUI();
    }

    private void initializeUI() {
        JFrame frame = new JFrame("Sistema de Gestão de Tripulação");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 600);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.WHITE);

        idField = createStyledTextField();
        nameField = createStyledTextField();
        phoneField = createStyledTextField();
        shiftField = createStyledTextField();
        experienceField = createStyledTextField();
        rankField = createStyledTextField();
        messageLabel = new JLabel(" ", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 14));

        crewListDisplay = new JList<>(crewListModel);
        crewListDisplay.setFont(new Font("Monospaced", Font.PLAIN, 14));
        crewListDisplay.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        crewListDisplay.addListSelectionListener(e -> populateFieldsFromSelection());

        JPanel inputPanel = createInputPanel();
        JPanel buttonPanel = createButtonPanel();
        JScrollPane listScrollPane = new JScrollPane(crewListDisplay);
        listScrollPane.setPreferredSize(new Dimension(400, 0));

        JLabel listTitle = new JLabel("Lista de Tripulantes", SwingConstants.CENTER);
        listTitle.setFont(new Font("Arial", Font.BOLD, 16));
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.add(listTitle, BorderLayout.NORTH);
        listPanel.add(listScrollPane, BorderLayout.CENTER);

        JPanel messagePanel = new JPanel();
        messagePanel.setBackground(Color.WHITE);
        messagePanel.add(messageLabel);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 15));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(inputPanel, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(messagePanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(listPanel, BorderLayout.EAST);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Dados do Tripulante"),
                new EmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        int row = 1; // Controla as linhas para organizar os inputs

        // Loop para exibir dados dos tripulantes
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")) {
            String sql = "SELECT * FROM crew WHERE id_Flight = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, flight.getId_Flight());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int id_Flight = rs.getInt("id_Flight");
                String name = rs.getString("nome");
                String shift = rs.getString("shift");
                int experience = rs.getInt("experience");
                String rank = rs.getString("ranq");

                Crew crewMember;
                if (rank == null) {
                    crewMember = new Crew.Assistant(id, name, shift, experience);
                } else {
                    Crew.Pilot pilot = new Crew.Pilot(id, name, shift, experience, rank);
                    crewMember = pilot;
                }

                // Adiciona tripulante à lista
                crewMembers.add(crewMember);
                crewListModel.addElement(formatCrewForList(crewMember));

                // Mostra os dados no painel
                JLabel crewLabel = new JLabel();
                crewLabel.setFont(new Font("Arial", Font.PLAIN, 12));


                inputPanel.add(crewLabel, gbc);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Adiciona as labels e os inputs organizados
        addFormField(inputPanel, "ID do Tripulante:", idField, 0, gbc);
        addFormField(inputPanel, "Nome:", nameField, 1, gbc);
        addFormField(inputPanel, "Telefone:", phoneField, 2, gbc);
        addFormField(inputPanel, "Turno:", shiftField, 3, gbc);
        addFormField(inputPanel, "Experiência (anos):", experienceField, 4, gbc);
        addFormField(inputPanel, "Cargo (Opcional):", rankField, 5, gbc);

        return inputPanel;
    }




    private void addFormField(JPanel panel, String labelText, JTextField field, int row, GridBagConstraints gbc) {
        gbc.gridy = row;

        // Adiciona a label
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(label, gbc);

        // Adiciona o campo de texto
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        field.setPreferredSize(new Dimension(200, 30)); // Define o tamanho do input
        panel.add(field, gbc);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton addButton = createStyledButton("Adicionar Tripulante");
        addButton.addActionListener(e -> addCrew());
        buttonPanel.add(addButton);

        JButton editButton = createStyledButton("Editar Tripulante");
        editButton.addActionListener(e -> editCrew());
        buttonPanel.add(editButton);

        JButton deleteButton = createStyledButton("Remover Tripulante");
        deleteButton.addActionListener(e -> deleteCrew());
        buttonPanel.add(deleteButton);

        return buttonPanel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(200, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
            }
        });

        return button;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(200, 30));
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        return field;
    }

    private void addCrew() {
        try {
            validateRequiredFields();
            id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            int phone = Integer.parseInt(phoneField.getText());
            String shift = shiftField.getText();
            int experience = Integer.parseInt(experienceField.getText());
            String ranq = rankField.getText().trim();

            Crew crewMember;
            if (ranq.isEmpty()) {
                crewMember = new Crew.Assistant(id, name, shift, experience);
            } else {
                Crew.Pilot pilot = new Crew.Pilot(id, name, shift, experience,ranq);
                crewMember = pilot;
            }

            crewMembers.add(crewMember);
            crewListModel.addElement(formatCrewForList(crewMember));
            showMessage("Tripulante adicionado com sucesso!", SUCCESS_COLOR);
            clearFields();
            Main.saveCrewData(id, flight.getId_Flight(), name, shift, experience, ranq);
        } catch (IllegalArgumentException e) {
            showMessage(e.getMessage(), ERROR_COLOR);
        }
    }

    private void editCrew() {
        try {
            int selectedIndex = crewListDisplay.getSelectedIndex();
            if (selectedIndex == -1) {
                throw new IllegalArgumentException("Por favor, selecione um tripulante na lista para editar.");
            }

            validateRequiredFields();

            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            int phone = Integer.parseInt(phoneField.getText());
            String shift = shiftField.getText();
            int experience = Integer.parseInt(experienceField.getText());
            String ranq = rankField.getText();

            Crew crewMember;
            if (ranq.isEmpty()) {
                crewMember = new Crew.Assistant(id, name, shift, experience);
            } else {
                Crew.Pilot pilot = new Crew.Pilot(id, name, shift, experience,ranq);
                crewMember = pilot;
            }

            crewMembers.set(selectedIndex, crewMember);
            crewListModel.set(selectedIndex, formatCrewForList(crewMember));
            showMessage("Tripulante atualizado com sucesso!", SUCCESS_COLOR);
            Main.saveCrewData(id, flight.getId_Flight(), name, shift, experience, ranq);
            clearFields();
        } catch (Exception e) {
            showMessage(e.getMessage(), ERROR_COLOR);
        }
    }

    private void deleteCrew() {
        try {
            int selectedIndex = crewListDisplay.getSelectedIndex();
            Main.deleteCrewData(id);
            if (selectedIndex == -1) {
                throw new IllegalArgumentException("Por favor, selecione um tripulante na lista para remover.");
            }
            crewMembers.remove(selectedIndex);
            crewListModel.remove(selectedIndex);
            showMessage("Tripulante removido com sucesso!", SUCCESS_COLOR);
            clearFields();
        } catch (Exception e) {
            showMessage(e.getMessage(), ERROR_COLOR);
        }
    }

    private void populateFieldsFromSelection() {
        int selectedIndex = crewListDisplay.getSelectedIndex();
        if (selectedIndex != -1) {
            Crew selectedCrew = crewMembers.get(selectedIndex);

            idField.setText(String.valueOf(selectedCrew.getId_CrewMember()));
            nameField.setText(selectedCrew.getName());
            phoneField.setText(String.valueOf(selectedCrew.getPhoneNumber()));
            shiftField.setText(selectedCrew.getShift());
            experienceField.setText(String.valueOf(selectedCrew.getExperience()));

            if (selectedCrew instanceof Crew.Pilot) {
                rankField.setText(((Crew.Pilot) selectedCrew).getRank());
            } else {
                rankField.setText(""); // Campo de rank vazio para assistentes
            }
        }
    }

    private void validateRequiredFields() {
        if (idField.getText().isEmpty() || nameField.getText().isEmpty() || phoneField.getText().isEmpty() ||
                shiftField.getText().isEmpty() || experienceField.getText().isEmpty()) {
            throw new IllegalArgumentException("Por favor, preencha todos os campos obrigatórios.");
        }
    }

    private String formatCrewForList(Crew crewMember) {
        String base = String.format("ID: %d, Nome: %s, Telefone: %d, Turno: %s, Experiência: %d anos",
                crewMember.getId_CrewMember(), crewMember.getName(), crewMember.getPhoneNumber(),
                crewMember.getShift(), crewMember.getExperience());
        if (crewMember instanceof Crew.Pilot) {
            base += ", Cargo: " + ((Crew.Pilot) crewMember).getRank();
        }
        return base;
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        phoneField.setText("");
        shiftField.setText("");
        experienceField.setText("");
        rankField.setText("");
    }

    private void showMessage(String message, Color color) {
        messageLabel.setText(message);
        messageLabel.setForeground(color);
    }
/*
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GestaoTripulacao::new);
    }

 */
}
