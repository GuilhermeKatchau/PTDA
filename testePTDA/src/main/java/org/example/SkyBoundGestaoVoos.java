package org.example;

import javax.swing.*;
import java.awt.*; // Importa GridLayout e BorderLayout

public class SkyBoundGestaoVoos extends JFrame {
    // Declaração das variáveis globais
    private final JTextField idAviao;
    private final JTextField origem;
    private final JTextField destino;
    private final DefaultListModel<String> listaVoos;

    public SkyBoundGestaoVoos() {
        // Configurações da janela
        setTitle("Gestão de Voos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Criação painel do formulário
        JPanel formPanel = new JPanel(new GridLayout(4, 4, 10, 10));

        // Inicializa os campos de texto
        idAviao = new JTextField();
        origem = new JTextField();
        destino = new JTextField();

        formPanel.add(new JLabel("ID do Avião:"));
        formPanel.add(idAviao);

        formPanel.add(new JLabel("ID do Voo:"));
        formPanel.add(new JTextField());

        formPanel.add(new JLabel("Code Name:"));
        formPanel.add(new JTextField());

        formPanel.add(new JLabel("Cidade de Origem:"));
        formPanel.add(origem);

        formPanel.add(new JLabel("Cidade de Destino:"));
        formPanel.add(destino);

        formPanel.add(new JLabel("Limite de Passageiros:"));
        formPanel.add(new JTextField());

        // Campos de Data e Hora
        JSpinner tempoPartida = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor1 = new JSpinner.DateEditor(tempoPartida, "dd/MM/yyyy HH:mm");
        tempoPartida.setEditor(editor1);

        JSpinner tempoChegada = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor2 = new JSpinner.DateEditor(tempoChegada, "dd/MM/yyyy HH:mm");
        tempoChegada.setEditor(editor2);

        formPanel.add(new JLabel("Tempo de Partida:"));
        formPanel.add(tempoPartida);

        formPanel.add(new JLabel("Tempo de Chegada:"));
        formPanel.add(tempoChegada);

        // Botão de submissão de voos
        JButton btnAdicionar = new JButton("Adicionar Voo");
        formPanel.add(btnAdicionar);

        // Adiciona o painel ao topo da janela
        add(formPanel, BorderLayout.NORTH);

        // Inicializa a lista de voos
        listaVoos = new DefaultListModel<>();
        JList<String> voosCadastrados = new JList<>(listaVoos);
        JScrollPane scrollPane = new JScrollPane(voosCadastrados);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Voos Cadastrados"));

        add(scrollPane, BorderLayout.CENTER);

        // Evento do botão
        btnAdicionar.addActionListener(e -> {
            String voo = "ID: " + idAviao.getText() +
                    " | Origem: " + origem.getText() +
                    " | Destino: " + destino.getText();
            listaVoos.addElement(voo);
            limparCampos();
        });

        // Botão de remoção de voos
        JButton btnRemover = new JButton("Remover Voo");
        btnRemover.addActionListener(e -> {
            int selectedIndex = voosCadastrados.getSelectedIndex();
            if (selectedIndex != -1) {
                listaVoos.remove(selectedIndex);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um voo para remover.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(btnRemover, BorderLayout.SOUTH);

        // Valaidação para que todos os campos sejam preenchidos
        if (idAviao.getText().isEmpty() || origem.getText().isEmpty() || destino.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Torna a janela visível
        setVisible(true);
    }

    // Método para limpar os campos
    private void limparCampos() {
        idAviao.setText("");
        origem.setText("");
        destino.setText("");
    }

    public static void main(String[] args) {
        new SkyBoundGestaoVoos();
    }
}
