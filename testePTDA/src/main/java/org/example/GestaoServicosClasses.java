package org.example;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class GestaoServicosClasses extends JFrame {
    // listas de objetos
    private final ArrayList<Class> classList = new ArrayList<>();
    private final ArrayList<String> serviceList = new ArrayList<>();

    // componentes visuais
    private final DefaultListModel<String> classListModel = new DefaultListModel<>();
    private final DefaultListModel<String> serviceListModel = new DefaultListModel<>();
    private final JList<String> classJlist = new JList<>(classListModel);
    private final JList<String> serviceJList = new JList<>(serviceListModel);

    public GestaoServicosClasses() {
        setTitle("gestao de classes e servicos");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // dividir a interface em classes e servicos
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                criarPainelClasses(), criarPainelServicos());
        add(splitPane, BorderLayout.CENTER);

        // painel de botoes
        JPanel botoesPanel = criarPainelBotoes();
        add(botoesPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void adicionarClasseInterativo() {
        JFrame frameAdicionarClasse = new JFrame("adicionar nova classe");
        frameAdicionarClasse.setSize(400, 600);
        frameAdicionarClasse.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // campo para o nome da classe
        JLabel lblNomeClasse = new JLabel("nome da classe:");
        JTextField txtNomeClasse = new JTextField();
        txtNomeClasse.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        // campo para o preco
        JLabel lblPreco = new JLabel("preco da classe:");
        JTextField txtPreco = new JTextField();
        txtPreco.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        // campo para a capacidade
        JLabel lblCapacidade = new JLabel("capacidade de assentos:");
        JTextField txtCapacidade = new JTextField();
        txtCapacidade.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        // lista de servicos
        JLabel lblServicos = new JLabel("servicos da classe:");
        DefaultListModel<String> servicosModel = new DefaultListModel<>();
        JList<String> listServicos = new JList<>(servicosModel);
        listServicos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollServicos = new JScrollPane(listServicos);
        scrollServicos.setPreferredSize(new Dimension(300, 100));

        // campo para adicionar novos servicos
        JTextField txtNovoServico = new JTextField();
        txtNovoServico.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        JButton btnAdicionarServico = new JButton("adicionar servico");
        btnAdicionarServico.addActionListener(e -> {
            String servico = txtNovoServico.getText().trim();
            if (!servico.isEmpty()) {
                servicosModel.addElement(servico);
                txtNovoServico.setText("");
            }
        });

        JButton btnRemoveService = new JButton("remover servico selecionado");
        btnRemoveService.addActionListener(e -> {
            int selectedIndex = listServicos.getSelectedIndex();
            if (selectedIndex != -1) {
                servicosModel.remove(selectedIndex);
            }
        });

        // botao para salvar a classe
        JButton btnSaveClass = new JButton("salvar classe");
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
                    throw new IllegalArgumentException("preencha todos os campos corretamente.");
                }

                // criar a nova classe
                Class novaClasse = new Class(nomeClasse, precoClasse, capacidadeClasse, servicosClasse);
                classList.add(novaClasse);
                classListModel.addElement(nomeClasse);

                // salvar a classe no base de dados
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://estga-dev.ua.pt:3306/PTDA24_BD_05", "PTDA24_05", "Potm%793")) {
                    String sql = "INSERT INTO class (nome, price, capacity, services) VALUES (?, ?, ?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    stmt.setString(1, nomeClasse);
                    stmt.setDouble(2, precoClasse);
                    stmt.setInt(3, capacidadeClasse);
                    stmt.setString(4, String.join(",", servicosClasse));
                    stmt.executeUpdate();

                    // gerar e salvar assentos no banco de dados
                    ResultSet generatedKeys = stmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int classId = generatedKeys.getInt(1);
                        gerarAssentos(conn, classId, capacidadeClasse, precoClasse);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "erro ao adicionar classe: " + ex.getMessage());
                }

                JOptionPane.showMessageDialog(this, "classe adicionada com sucesso!");
                frameAdicionarClasse.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frameAdicionarClasse, "erro ao adicionar classe: " + ex.getMessage(), "erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // adicionar componentes ao painel
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
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // espacamento
        panel.add(btnSaveClass);

        frameAdicionarClasse.add(panel);
        frameAdicionarClasse.setVisible(true);
    }

    private void gerarAssentos(Connection conn, int classId, int capacidade, double preco) throws SQLException {
        String sql = "INSERT INTO seat (id_Seat, price, class) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 1; i <= capacidade; i++) {
                stmt.setInt(1, i); // id do assento
                stmt.setDouble(2, preco); // preco do assento
                stmt.setInt(3, classId); // id da classe
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private JPanel criarPainelAssentos(int capacidade) {
        JPanel panel = new JPanel(new GridLayout(0, 3)); // 3 colunas: 1 para a fila esquerda e 2 para as filas direitas

        int assentosPorFila = capacidade / 3;
        int assentoAtual = 1;

        // fila do lado esquerdo
        for (int i = 1; i <= assentosPorFila; i++) {
            JButton btnAssento = new JButton(String.valueOf(assentoAtual));
            panel.add(btnAssento);
            assentoAtual++;
        }

        // filas do lado direito
        for (int i = 1; i <= 2 * assentosPorFila; i++) {
            JButton btnAssento = new JButton(String.valueOf(assentoAtual));
            panel.add(btnAssento);
            assentoAtual++;
        }

        return panel;
    }

    private JPanel criarPainelClasses() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("classes"));

        // adicionar mouseListener para o duplo clique
        classJlist.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) { // detectar duplo clique
                    int index = classJlist.locationToIndex(e.getPoint()); // obter indice do item clicado
                    if (index != -1) {
                        atualizarServicos(index); // atualizar painel de servicos
                    }
                }
            }
        });

        // carregar dados do banco de dados
        carregarDadosClasses();

        panel.add(new JScrollPane(classJlist), BorderLayout.CENTER);

        // botoes opcionais para gestao
        JPanel botoes = criarBotoesClasses();
        panel.add(botoes, BorderLayout.SOUTH);

        return panel;
    }

    // metodo para carregar classes e servicos do banco de dados
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
            JOptionPane.showMessageDialog(this, "erro ao carregar classes: " + e.getMessage(), "erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // metodo para criar botoes da lista de classes
    private JPanel criarBotoesClasses() {
        JPanel botoes = new JPanel();

        JButton btnAdicionarClasseInterativo = new JButton("adicionar classe");
        btnAdicionarClasseInterativo.addActionListener(e -> adicionarClasseInterativo());

        JButton btnRemoverClasse = new JButton("remover classe");
        btnRemoverClasse.addActionListener(e -> removerClasse());

        JButton btnDetalhesClasse = new JButton("detalhes da classe");
        btnDetalhesClasse.addActionListener(e -> visualizarDetalhesClasse());

        botoes.add(btnAdicionarClasseInterativo);
        botoes.add(btnRemoverClasse);
        botoes.add(btnDetalhesClasse);

        return botoes;
    }

    private JPanel criarPainelServicos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("servicos"));

        panel.add(new JScrollPane(serviceJList), BorderLayout.CENTER);
        return panel;
    }

    private JPanel criarPainelBotoes() {
        JPanel panel = new JPanel(new FlowLayout());
        JButton btnSair = new JButton("sair");
        btnSair.addActionListener(e -> System.exit(0));
        panel.add(btnSair);
        return panel;
    }

    private void removerClasse() {
        int index = classJlist.getSelectedIndex();
        if (index != -1) {
            classList.remove(index);
            classListModel.remove(index);
            JOptionPane.showMessageDialog(this, "classe removida com sucesso!");
        } else {
            JOptionPane.showMessageDialog(this, "selecione uma classe para remover.");
        }
    }

    public void abrirTelaSelecaoAssentos(Class classeSelecionada, int idTicket, String namePassenger, double price, int idFlight, int numberOfPassengers) {
        SwingUtilities.invokeLater(() -> new SkyBoundAdicionarAssento(idTicket, namePassenger, price, classeSelecionada, idFlight, numberOfPassengers));
    }

    private void visualizarDetalhesClasse() {
        int index = classJlist.getSelectedIndex();
        if (index != -1) {
            Class classe = classList.get(index);
            StringBuilder detalhes = new StringBuilder("detalhes da classe:\n");
            detalhes.append("nome: ").append(classe.getClassName()).append("\n");
            detalhes.append("preco: ").append(classe.getPrice()).append("\n");
            detalhes.append("capacidade: ").append(classe.getSeatCapacity()).append("\n");
            detalhes.append("servicos:\n");
            for (String servico : classe.getServices()) {
                detalhes.append("- ").append(servico).append("\n");
            }

            // botao para abrir a tela de selecao de assentos
            JButton btnSelecionarAssentos = new JButton("selecionar assentos");
            btnSelecionarAssentos.addActionListener(e -> {
                int idTicket = 1; // exemplo de id do ticket
                String namePassenger = "joao silva"; // exemplo de nome do passageiro
                double price = classe.getPrice(); // preco da classe
                int idFlight = 123; // exemplo de id do voo
                int numberOfPassengers = 1; // exemplo de numero de passageiros
                abrirTelaSelecaoAssentos(classe, idTicket, namePassenger, price, idFlight, numberOfPassengers);
            });

            // exibir os detalhes e o botao
            JOptionPane.showMessageDialog(this, new Object[]{detalhes.toString(), btnSelecionarAssentos}, "detalhes da classe", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "selecione uma classe para ver os detalhes.");
        }
    }

    private void atualizarServicos(int index) {
        serviceListModel.clear(); // limpa os servicos no painel de servicos

        if (index != -1) { // verifica se o indice e valido
            Class classe = classList.get(index); // obtem a classe correspondente
            for (String servico : classe.getServices()) { // adiciona cada servico ao modelo da lista
                serviceListModel.addElement(servico);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GestaoServicosClasses::new);
    }
}