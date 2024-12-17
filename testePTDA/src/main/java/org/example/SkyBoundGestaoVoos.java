package org.example;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Date;

public class SkyBoundGestaoVoos extends JFrame {

    // Declaração dos campos do formulário
    private final JTextField idAviao, idVoo, codeName, origem, destino;
    private final JFormattedTextField limitePassageiros;
    private final JSpinner tempoPartida, tempoChegada;
    private final DefaultListModel<String> listaVoos;
    private final JList<String> voosCadastrados;

    public SkyBoundGestaoVoos() {
        // Configurações da Janela
        setTitle("Gestão de Voos");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Inicializa os campos do formulário
        idAviao = new JTextField();
        idVoo = new JTextField();
        codeName = new JTextField();
        origem = new JTextField();
        destino = new JTextField();

        // Campo numérico para Limite de Passageiros
        NumberFormat numberFormat = NumberFormat.getIntegerInstance();
        limitePassageiros = new JFormattedTextField(numberFormat);
        limitePassageiros.setValue(0);

        // Inicializa os campos de data e hora
        tempoPartida = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorPartida = new JSpinner.DateEditor(tempoPartida, "dd/MM/yyyy HH:mm");
        tempoPartida.setEditor(editorPartida);

        tempoChegada = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorChegada = new JSpinner.DateEditor(tempoChegada, "dd/MM/yyyy HH:mm");
        tempoChegada.setEditor(editorChegada);

        listaVoos = new DefaultListModel<>();
        voosCadastrados = new JList<>(listaVoos);

        // Criação dos Componentes
        JPanel formPanel = criarFormulario();
        JScrollPane scrollPane = criarListaVoos();
        JPanel buttonPanel = criarBotoes();

        // Adiciona componentes à janela
        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Método para criar o formulário
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

    // Método para criar a lista de voos
    private JScrollPane criarListaVoos() {
        JScrollPane scrollPane = new JScrollPane(voosCadastrados);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Voos Cadastrados"));
        return scrollPane;
    }

    // Método para criar os botões
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

    // Método para adicionar um voo
    private void adicionarVoo() {
        try {
            int id_Airplane = Integer.parseInt(idAviao.getText());
            int id_Flight = Integer.parseInt(idVoo.getText());
            String codename = codeName.getText();
            String source = origem.getText();
            String destination = destino.getText();
            int maxPassengers = Integer.parseInt(limitePassageiros.getText());
            Date hTakeOff = (Date) tempoPartida.getValue();
            Date hLanding = (Date) tempoChegada.getValue();

            Flight.addFlight(id_Airplane, id_Flight, codename, source, destination, maxPassengers, hTakeOff, hLanding);
            listaVoos.addElement("ID Avião: " + id_Airplane + " | ID Voo: " + id_Flight + " | Code Name: " + codename + " | Origem: " + source + " | Destino: " + destination + " | Partida: " + hTakeOff + " | Chegada: " + hLanding + " | Limite: " + maxPassengers);
            limparCampos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos corretamente!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para remover um voo selecionado
    private void removerVoo() {
        int selectedIndex = voosCadastrados.getSelectedIndex();
        if (selectedIndex != -1) {
            listaVoos.remove(selectedIndex);
            JOptionPane.showMessageDialog(this, "Voo removido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um voo para remover!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para limpar os campos do formulário
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
