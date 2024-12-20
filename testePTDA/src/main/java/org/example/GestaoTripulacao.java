package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GestaoTripulacao {
    private JTextField idField, nameField, phoneField, shiftField, experienceField, rankField;
    private JLabel messageLabel;
    private final DefaultListModel<String> crewListModel;
    private JList<String> crewListDisplay;
    private static final Color PRIMARY_COLOR = new Color(51, 153, 255);
    private static final Color SUCCESS_COLOR = new Color(75, 181, 67);
    private static final Color ERROR_COLOR = new Color(220, 53, 69);

    public GestaoTripulacao() {
        crewListModel = new DefaultListModel<>();
        initializeUI();
    }

    private void initializeUI() {
        JFrame frame = new JFrame("Sistema de Gestão de Tripulação");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 800);
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

        addFormField(inputPanel, "ID:", idField, 0, gbc);
        addFormField(inputPanel, "Nome:", nameField, 1, gbc);
        addFormField(inputPanel, "Número de Telefone:", phoneField, 2, gbc);
        addFormField(inputPanel, "Turno:", shiftField, 3, gbc);
        addFormField(inputPanel, "Experiência (Anos):", experienceField, 4, gbc);
        addFormField(inputPanel, "Cargo (Opcional):", rankField, 5, gbc);

        return inputPanel;
    }

    private void addFormField(JPanel panel, String labelText, JTextField field, int row, GridBagConstraints gbc) {
        gbc.gridy = row;

        gbc.gridx = 0;
        gbc.weightx = 0.3;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
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
            String crewMember = String.format("ID: %s, Nome: %s, Telefone: %s, Turno: %s, Experiência: %s, Cargo: %s",
                    idField.getText(),
                    nameField.getText(),
                    phoneField.getText(),
                    shiftField.getText(),
                    experienceField.getText(),
                    rankField.getText());

            crewListModel.addElement(crewMember);
            showMessage("Tripulante adicionado com sucesso!", SUCCESS_COLOR);
            clearFields();
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
            String updatedCrew = String.format("ID: %s, Nome: %s, Telefone: %s, Turno: %s, Experiência: %s, Cargo: %s",
                    idField.getText(),
                    nameField.getText(),
                    phoneField.getText(),
                    shiftField.getText(),
                    experienceField.getText(),
                    rankField.getText());

            crewListModel.set(selectedIndex, updatedCrew);
            showMessage("Tripulante atualizado com sucesso!", SUCCESS_COLOR);
            clearFields();
        } catch (IllegalArgumentException e) {
            showMessage(e.getMessage(), ERROR_COLOR);
        }
    }

    private void deleteCrew() {
        try {
            int selectedIndex = crewListDisplay.getSelectedIndex();
            if (selectedIndex == -1) {
                throw new IllegalArgumentException("Por favor, selecione um tripulante na lista para remover.");
            }

            crewListModel.remove(selectedIndex);
            showMessage("Tripulante removido com sucesso!", SUCCESS_COLOR);
            clearFields();
        } catch (IllegalArgumentException e) {
            showMessage(e.getMessage(), ERROR_COLOR);
        }
    }

    private void populateFieldsFromSelection() {
        int selectedIndex = crewListDisplay.getSelectedIndex();
        if (selectedIndex != -1) {
            String selectedCrew = crewListModel.get(selectedIndex);
            String[] parts = selectedCrew.split(", ");
            idField.setText(parts[0].split(": ")[1]);
            nameField.setText(parts[1].split(": ")[1]);
            phoneField.setText(parts[2].split(": ")[1]);
            shiftField.setText(parts[3].split(": ")[1]);
            experienceField.setText(parts[4].split(": ")[1]);
            rankField.setText(parts.length > 5 ? parts[5].split(": ")[1] : "");
        }
    }

    private void validateRequiredFields() {
        if (idField.getText().isEmpty() || nameField.getText().isEmpty() || phoneField.getText().isEmpty() ||
                shiftField.getText().isEmpty() || experienceField.getText().isEmpty()) {
            throw new IllegalArgumentException("Por favor, preencha todos os campos obrigatórios.");
        }

        if (!phoneField.getText().matches("\\d+")) {
            throw new IllegalArgumentException("O número de telefone deve conter apenas dígitos.");
        }

        if (!experienceField.getText().matches("\\d+")) {
            throw new IllegalArgumentException("Os anos de experiência devem conter apenas dígitos.");
        }
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GestaoTripulacao::new);
    }
}
