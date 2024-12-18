package org.example;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SkyBoundGestaoVoos extends JFrame {

    private final JTextField idAviao, idVoo, codeName, origem, destino;
    private final JFormattedTextField limitePassageiros;
    private final JSpinner tempoPartida, tempoChegada;
    private final DefaultListModel<String> listaVoos;
    private final JList<String> voosCadastrados;

    public SkyBoundGestaoVoos() {
        setTitle("Gestão de Voos");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        idAviao = new JTextField();
        idVoo = new JTextField();
        codeName = new JTextField();
        origem = new JTextField();
        destino = new JTextField();

        NumberFormat numberFormat = NumberFormat.getIntegerInstance();
        limitePassageiros = new JFormattedTextField(numberFormat);
        limitePassageiros.setValue(0);

        tempoPartida = new JSpinner(new SpinnerDateModel());
        tempoPartida.setEditor(new JSpinner.DateEditor(tempoPartida, "dd/MM/yyyy HH:mm"));

        tempoChegada = new JSpinner(new SpinnerDateModel());
        tempoChegada.setEditor(new JSpinner.DateEditor(tempoChegada, "dd/MM/yyyy HH:mm"));

        listaVoos = new DefaultListModel<>();
        voosCadastrados = new JList<>(listaVoos);

        JPanel formPanel = criarFormulario();
        JScrollPane scrollPane = criarListaVoos();
        JPanel buttonPanel = criarBotoes();

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        carregarVoos();
        setVisible(true);
    }

    private JPanel criarFormulario() {
        JPanel formPanel = new JPanel(new GridLayout(5, 4, 10, 10));

        formPanel.add(new JLabel("ID do Avião:"));
        formPanel.add(idAviao);

        formPanel.add(new JLabel("ID do Voo:"));
        formPanel.add(idVoo);

        formPanel.add(new JLabel("Code Name:"));
        formPanel.add(codeName);

        formPanel.add(new JLabel("Cidade de Origem:"));
        formPanel.add(origem);

        formPanel.add(new JLabel("Cidade de Destino:"));
        formPanel.add(destino);

        formPanel.add(new JLabel("Limite de Passageiros:"));
        formPanel.add(limitePassageiros);

        formPanel.add(new JLabel("Tempo de Partida:"));
        formPanel.add(tempoPartida);

        formPanel.add(new JLabel("Tempo de Chegada:"));
        formPanel.add(tempoChegada);

        return formPanel;
    }

    private JScrollPane criarListaVoos() {
        JScrollPane scrollPane = new JScrollPane(voosCadastrados);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Voos Cadastrados"));
        return scrollPane;
    }

    private JPanel criarBotoes() {
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton btnAdicionar = new JButton("Adicionar Voo");
        btnAdicionar.addActionListener(e -> adicionarVoo());

        JButton btnRemover = new JButton("Remover Voo");
        btnRemover.addActionListener(e -> removerVoo());

        buttonPanel.add(btnAdicionar);
        buttonPanel.add(btnRemover);

        return buttonPanel;
    }

    private void adicionarVoo() {
        try {
            // Verifica se todos os campos estão preenchidos
            if (idAviao.getText().isEmpty() || idVoo.getText().isEmpty() || codeName.getText().isEmpty()
                    || origem.getText().isEmpty() || destino.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verifica se ID do Avião e ID do Voo são numéricos
            int id_Airplane = Integer.parseInt(idAviao.getText());
            int id_Flight = Integer.parseInt(idVoo.getText());

            String codename = codeName.getText();
            String source = origem.getText();
            String destination = destino.getText();
            int maxPassengers = Integer.parseInt(limitePassageiros.getText());

            Date hTakeOff = (Date) tempoPartida.getValue();
            Date hLanding = (Date) tempoChegada.getValue();

            // Verifica se a hora de partida é anterior à de chegada
            if (!hTakeOff.before(hLanding) || (hLanding.getTime() - hTakeOff.getTime()) < 60000) {
                JOptionPane.showMessageDialog(this, "O tempo de partida deve ser pelo menos 1 minuto antes do de chegada!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Adicionar o voo ao FlightManager
            Flight flight = new Flight(id_Airplane, id_Flight, codename, source, destination, maxPassengers, hTakeOff, hLanding);
            FlightManager.getInstance().addFlight(flight);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            listaVoos.addElement("ID Avião: " + id_Airplane
                    + " | ID Voo: " + id_Flight
                    + " | Code Name: " + codename
                    + " | Origem: " + source
                    + " | Destino: " + destination
                    + " | Partida: " + dateFormat.format(hTakeOff)
                    + " | Chegada: " + dateFormat.format(hLanding)
                    + " | Limite: " + maxPassengers);

            JOptionPane.showMessageDialog(this, "Voo adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Os campos ID do Avião, ID do Voo devem ser numéricos!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerVoo() {
        int selectedIndex = voosCadastrados.getSelectedIndex();
        if (selectedIndex != -1) {
            FlightManager.getInstance().getFlights().remove(selectedIndex);
            listaVoos.remove(selectedIndex);
            JOptionPane.showMessageDialog(this, "Voo removido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um voo para remover!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarVoos() {
        for (Flight flight : FlightManager.getInstance().getFlights()) {
            listaVoos.addElement(flight.toString());
        }
    }

    private void limparCampos() {
        idAviao.setText("");
        idVoo.setText("");
        codeName.setText("");
        origem.setText("");
        destino.setText("");
        limitePassageiros.setValue(0);
        tempoPartida.setValue(new Date());
        tempoChegada.setValue(new Date());
    }

    public static void main(String[] args) {
        new SkyBoundGestaoVoos();
    }
}
