package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GestaoServicosClasses extends JFrame {
    // Listas de objetos
    private final ArrayList<Class> listaClasses = new ArrayList<>();
    private final ArrayList<Service> listaServicos = new ArrayList<>();

    // Componentes visuais
    private final DefaultListModel<String> listaClassesModel = new DefaultListModel<>();
    private final DefaultListModel<String> listaServicosModel = new DefaultListModel<>();
    private final JList<String> listaClassesJList = new JList<>(listaClassesModel);
    private final JList<String> listaServicosJList = new JList<>(listaServicosModel);

    public GestaoServicosClasses() {
        setTitle("Gestão de Classes e Serviços");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Dividir a interface em Classes e Serviços
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                criarPainelClasses(), criarPainelServicos());
        add(splitPane, BorderLayout.CENTER);

        // Painel de botões
        JPanel botoesPanel = criarPainelBotoes();
        add(botoesPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel criarPainelClasses() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Classes"));

        panel.add(new JScrollPane(listaClassesJList), BorderLayout.CENTER);

        JButton btnAdicionarClasse = new JButton("Adicionar Classe");
        btnAdicionarClasse.addActionListener(e -> adicionarClasse());

        JButton btnRemoverClasse = new JButton("Remover Classe");
        btnRemoverClasse.addActionListener(e -> removerClasse());

        JButton btnDetalhesClasse = new JButton("Detalhes da Classe");
        btnDetalhesClasse.addActionListener(e -> visualizarDetalhesClasse());

        JPanel botoes = new JPanel();
        botoes.add(btnAdicionarClasse);
        botoes.add(btnRemoverClasse);
        botoes.add(btnDetalhesClasse);

        panel.add(botoes, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel criarPainelServicos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Serviços"));

        panel.add(new JScrollPane(listaServicosJList), BorderLayout.CENTER);
        return panel;
    }

    private JPanel criarPainelBotoes() {
        JPanel panel = new JPanel(new FlowLayout());
        JButton btnSair = new JButton("Sair");
        btnSair.addActionListener(e -> System.exit(0));
        panel.add(btnSair);
        return panel;
    }

    private void adicionarClasse() {
        try {
            String nome = JOptionPane.showInputDialog("Nome da Classe:");
            double preco = Double.parseDouble(JOptionPane.showInputDialog("Preço da Classe:"));
            int capacidade = Integer.parseInt(JOptionPane.showInputDialog("Capacidade de Assentos:"));

            ArrayList<Service> servicosClasse = new ArrayList<>();
            while (true) {
                int resposta = JOptionPane.showConfirmDialog(this, "Deseja adicionar um serviço à classe?");
                if (resposta != JOptionPane.YES_OPTION) break;

                String nomeServico = JOptionPane.showInputDialog("Nome do Serviço:");
                int idServico = listaServicos.size() + 1;
                String descricaoServico = JOptionPane.showInputDialog("Descrição do Serviço:");

                Service novoServico = new Service(nomeServico, idServico, descricaoServico);
                servicosClasse.add(novoServico);
            }

            Class novaClasse = new Class(nome, preco, capacidade, servicosClasse);
            listaClasses.add(novaClasse);
            listaClassesModel.addElement(nome);
            JOptionPane.showMessageDialog(this, "Classe adicionada com sucesso!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar classe: " + e.getMessage());
        }
    }

    private void removerClasse() {
        int index = listaClassesJList.getSelectedIndex();
        if (index != -1) {
            listaClasses.remove(index);
            listaClassesModel.remove(index);
            JOptionPane.showMessageDialog(this, "Classe removida com sucesso!");
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma classe para remover.");
        }
    }

    private void visualizarDetalhesClasse() {
        int index = listaClassesJList.getSelectedIndex();
        if (index != -1) {
            Class classe = listaClasses.get(index);
            StringBuilder detalhes = new StringBuilder("Detalhes da Classe:\n");
            detalhes.append("Nome: ").append(classe.getClassName()).append("\n");
            detalhes.append("Preço: ").append(classe.getPrice()).append("\n");
            detalhes.append("Capacidade: ").append(classe.getSeatCapacity()).append("\n");
            detalhes.append("Serviços:\n");
            for (Service servico : classe.getServices()) {
                detalhes.append("- ").append(servico.getName()).append(": ").append(servico.getDescription()).append("\n");
            }
            JOptionPane.showMessageDialog(this, detalhes.toString());
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma classe para ver os detalhes.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GestaoServicosClasses::new);
    }
}
