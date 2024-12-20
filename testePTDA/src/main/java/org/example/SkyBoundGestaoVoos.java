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

    private JScrollPane FlightList() {
        JScrollPane scrollPane = new JScrollPane(voosCadastrados);
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
            int id_Airplane = Integer.parseInt(idAviao.getText());
            int id_Flight = Integer.parseInt(idVoo.getText());
            String codename = codeName.getText();
            String source = origem.getText();
            String destination = destino.getText();
            int maxPassengers = Integer.parseInt(limitePassageiros.getText());
            Date hTakeOff = (Date) tempoPartida.getValue();
            Date hLanding = (Date) tempoChegada.getValue();

            Flight.addFlight(id_Airplane, id_Flight, maxPassengers, hTakeOff, hLanding, destination, source, codename);
            Main.salvarDadosFlight(id_Airplane, id_Flight, maxPassengers, hTakeOff, hLanding, destination, source, codename);
            loadFlights();
            clearFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Os campos ID do Avião, ID do Voo devem ser numéricos!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeFlight() {
        int selectedIndex = voosCadastrados.getSelectedIndex();
        if (selectedIndex != -1) {
            Flight.removeFlight(selectedIndex);
            loadFlights();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um voo para remover!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadFlights() {
        listaVoos.clear();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//TEM DE SE TROCAR PARA APARECER OS VOOS DA BD !!!!!
        for (Flight flight : Flight.getFlights()) {
            listaVoos.addElement("ID Avião: " + flight.getId_Airplane()
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
        int selectedIndex = voosCadastrados.getSelectedIndex();
        if (selectedIndex != -1) {
            Flight selectedFlight = Flight.getFlights().get(selectedIndex);
            new GestaoTripulacao();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um voo para continuar!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
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
