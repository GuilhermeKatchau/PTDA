package org.example;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

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

                // Salvar a classe no banco de dados
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")) {
                    String sql = "INSERT INTO class (nome, price, capacity, services) VALUES (?, ?, ?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    stmt.setString(1, nomeClasse);
                    stmt.setDouble(2, precoClasse);
                    stmt.setInt(3, capacidadeClasse);
                    stmt.setString(4, String.join(",", servicosClasse));
                    stmt.executeUpdate();

                    // Gerar e salvar assentos no banco de dados
                    ResultSet generatedKeys = stmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int classId = generatedKeys.getInt(1);
                        gerarAssentos(conn, classId, capacidadeClasse, precoClasse);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao adicionar classe: " + ex.getMessage());
                }

                JOptionPane.showMessageDialog(this, "Classe adicionada com sucesso!");
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

    private void gerarAssentos(Connection conn, int classId, int capacidade, double preco) throws SQLException {
        String sql = "INSERT INTO seat (id_Seat, price, class) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 1; i <= capacidade; i++) {
                stmt.setInt(1, i);
                stmt.setDouble(2, preco);
                stmt.setInt(3, classId);
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private JPanel criarPainelClasses() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Classes"));

        // Adicionar MouseListener para o duplo clique
        classJlist.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) { // Detectar duplo clique
                    int index = classJlist.locationToIndex(e.getPoint()); // Obter índice do item clicado
                    if (index != -1) {
                        atualizarServicos(index); // Atualizar painel de serviços
                    }
                }
            }
        });

        // Carregar dados do banco de dados
        carregarDadosClasses();

        panel.add(new JScrollPane(classJlist), BorderLayout.CENTER);

        // Botões opcionais para gestão
        JPanel botoes = criarBotoesClasses();
        panel.add(botoes, BorderLayout.SOUTH);

        return panel;
    }

    // Método para carregar classes e serviços do banco de dados
    private void carregarDadosClasses() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")) {
            String sql = "SELECT * FROM class";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String nome = rs.getString("nome");
                double preco = rs.getDouble("price");
                int capacidade = rs.getInt("capacity");
                ArrayList<String> servicos = new ArrayList<>(Arrays.asList(rs.getString("services").split(",")));

                Class novaClasse = new Class(nome, preco, capacidade, servicos);
                classList.add(novaClasse);
                classListModel.addElement(nome);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar classes: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para criar botões da lista de classes
    private JPanel criarBotoesClasses() {
        JPanel botoes = new JPanel();

        JButton btnAdicionarClasseInterativo = new JButton("Adicionar Classe");
        btnAdicionarClasseInterativo.addActionListener(e -> adicionarClasseInterativo());

        JButton btnRemoverClasse = new JButton("Remover Classe");
        btnRemoverClasse.addActionListener(e -> removerClasse());

        JButton btnDetalhesClasse = new JButton("Detalhes da Classe");
        btnDetalhesClasse.addActionListener(e -> visualizarDetalhesClasse());

        botoes.add(btnAdicionarClasseInterativo);
        botoes.add(btnRemoverClasse);
        botoes.add(btnDetalhesClasse);

        return botoes;
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

    private void atualizarServicos(int index) {
        serviceListModel.clear(); // Limpa os serviços no painel de serviços

        if (index != -1) { // Verifica se o índice é válido
            Class classe = classList.get(index); // Obtém a classe correspondente
            for (String servico : classe.getServices()) { // Adiciona cada serviço ao modelo da lista
                serviceListModel.addElement(servico);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GestaoServicosClasses::new);
    }
}