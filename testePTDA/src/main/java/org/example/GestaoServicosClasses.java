package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GestaoServicosClasses extends JFrame {
    // Listas de objetos
    private final ArrayList<Class> classList = new ArrayList<>();
    private final ArrayList<String> serviceList = new ArrayList<>();

    // Componentes visuais
    private final DefaultListModel<String> classListModel = new DefaultListModel<>();
    private final DefaultListModel<String> serviceListModel = new DefaultListModel<>();
    private final JList<String> classJlist = new JList<>(classListModel);
    private final JList<String> serviceJList = new JList<>(serviceListModel);

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
    private void adicionarClasseInterativo() {
        JFrame frameAdicionarClasse = new JFrame("Adicionar Nova Classe");
        frameAdicionarClasse.setSize(400, 600);
        frameAdicionarClasse.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Campo para o nome da classe
        JLabel lblNomeClasse = new JLabel("Nome da Classe:");
        JTextField txtNomeClasse = new JTextField();
        txtNomeClasse.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        // Campo para o preço
        JLabel lblPreco = new JLabel("Preço da Classe:");
        JTextField txtPreco = new JTextField();
        txtPreco.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        // Campo para a capacidade
        JLabel lblCapacidade = new JLabel("Capacidade de Assentos:");
        JTextField txtCapacidade = new JTextField();
        txtCapacidade.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        // Lista de serviços
        JLabel lblServicos = new JLabel("Serviços da Classe:");
        DefaultListModel<String> servicosModel = new DefaultListModel<>();
        JList<String> listServicos = new JList<>(servicosModel);
        listServicos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollServicos = new JScrollPane(listServicos);
        scrollServicos.setPreferredSize(new Dimension(300, 100));

        // Campo para adicionar novos serviços
        JTextField txtNovoServico = new JTextField();
        txtNovoServico.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        JButton btnAdicionarServico = new JButton("Adicionar Serviço");
        btnAdicionarServico.addActionListener(e -> {
            String servico = txtNovoServico.getText().trim();
            if (!servico.isEmpty()) {
                servicosModel.addElement(servico);
                txtNovoServico.setText("");
            }
        });

        JButton btnRemoveService = new JButton("Remover Serviço Selecionado");
        btnRemoveService.addActionListener(e -> {
            int selectedIndex = listServicos.getSelectedIndex();
            if (selectedIndex != -1) {
                servicosModel.remove(selectedIndex);
            }
        });

        // Botão para salvar a classe
        JButton btnSaveClass = new JButton("Salvar Classe");
        btnSaveClass.addActionListener(e -> {
            try {
                String nomeClasse = txtNomeClasse.getText().trim();
                double precoClasse = Double.parseDouble(txtPreco.getText().trim());
                int capacidadeClasse = Integer.parseInt(txtCapacidade.getText().trim());

                ArrayList<String> servicosClasse = new ArrayList<>();
                for (int i = 0; i < servicosModel.size(); i++) {
                    servicosClasse.add(servicosModel.getElementAt(i));
                }

                if (nomeClasse.isEmpty() || capacidadeClasse <= 0 || precoClasse <= 0) {
                    throw new IllegalArgumentException("Preencha todos os campos corretamente.");
                }

                // Criar a nova classe
                Class novaClasse = new Class(nomeClasse, precoClasse, capacidadeClasse, servicosClasse);
                classList.add(novaClasse);
                classListModel.addElement(nomeClasse);

                JOptionPane.showMessageDialog(frameAdicionarClasse, "Classe adicionada com sucesso!");
                frameAdicionarClasse.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frameAdicionarClasse, "Erro ao adicionar classe: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Adicionar componentes ao painel
        panel.add(lblNomeClasse);
        panel.add(txtNomeClasse);
        panel.add(lblPreco);
        panel.add(txtPreco);
        panel.add(lblCapacidade);
        panel.add(txtCapacidade);
        panel.add(lblServicos);
        panel.add(scrollServicos);
        panel.add(txtNovoServico);
        panel.add(btnAdicionarServico);
        panel.add(btnRemoveService);

        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Espaçamento
        panel.add(btnSaveClass);

        frameAdicionarClasse.add(panel);
        frameAdicionarClasse.setVisible(true);
    }


    private JPanel criarPainelClasses() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Classes"));

        panel.add(new JScrollPane(classJlist), BorderLayout.CENTER);

        JButton btnAdicionarClasse = new JButton("Adicionar Classe");
        btnAdicionarClasse.addActionListener(e -> adicionarClasse());

        JButton btnRemoverClasse = new JButton("Remover Classe");
        btnRemoverClasse.addActionListener(e -> removerClasse());

        JButton btnDetalhesClasse = new JButton("Detalhes da Classe");
        btnDetalhesClasse.addActionListener(e -> visualizarDetalhesClasse());

        JButton btnAdicionarClasseInterativo = new JButton("Adicionar Classe Interativa");
        btnAdicionarClasseInterativo.addActionListener(e -> adicionarClasseInterativo());

        JButton btnAtualizarServicos = new JButton("Atualizar Serviços");
        btnAtualizarServicos.addActionListener(e -> atualizarServicos());

        JPanel botoes = new JPanel();
        botoes.add(btnAdicionarClasse);
        botoes.add(btnRemoverClasse);
        botoes.add(btnDetalhesClasse);
        botoes.add(btnAdicionarClasseInterativo);
        botoes.add(btnAtualizarServicos);

        panel.add(botoes, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel criarPainelServicos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Serviços"));

        panel.add(new JScrollPane(serviceJList), BorderLayout.CENTER);
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

            ArrayList<String> servicosClasse = new ArrayList<>();
            while (true) {
                int resposta = JOptionPane.showConfirmDialog(this, "Deseja adicionar um serviço à classe?");
                if (resposta != JOptionPane.YES_OPTION) break;

                String nomeServico = JOptionPane.showInputDialog("Nome ou Descrição do Serviço:");
                servicosClasse.add(nomeServico);
            }

            Class novaClasse = new Class(nome, preco, capacidade, servicosClasse);
            classList.add(novaClasse);
            classListModel.addElement(nome);
            JOptionPane.showMessageDialog(this, "Classe adicionada com sucesso!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar classe: " + e.getMessage());
        }
    }

    private void removerClasse() {
        int index = classJlist.getSelectedIndex();
        if (index != -1) {
            classList.remove(index);
            classListModel.remove(index);
            JOptionPane.showMessageDialog(this, "Classe removida com sucesso!");
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma classe para remover.");
        }
    }

    private void visualizarDetalhesClasse() {
        int index = classJlist.getSelectedIndex();
        if (index != -1) {
            Class classe = classList.get(index);
            StringBuilder detalhes = new StringBuilder("Detalhes da Classe:\n");
            detalhes.append("Nome: ").append(classe.getClassName()).append("\n");
            detalhes.append("Preço: ").append(classe.getPrice()).append("\n");
            detalhes.append("Capacidade: ").append(classe.getSeatCapacity()).append("\n");
            detalhes.append("Serviços:\n");
            for (String servico : classe.getServices()) {
                detalhes.append("- ").append(servico).append("\n");
            }
            JOptionPane.showMessageDialog(this, detalhes.toString());
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma classe para ver os detalhes.");
        }
    }

    private void atualizarServicos() {
        int index = classJlist.getSelectedIndex();
        serviceListModel.clear(); // Limpa a lista de serviços anterior

        if (index != -1) {
            Class classe = classList.get(index);
            for (String servico : classe.getServices()) {
                serviceListModel.addElement(servico);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GestaoServicosClasses::new);
    }
}
