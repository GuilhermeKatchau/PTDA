package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SkyBoundHomePage {
    public JPanel panel1; // painel principal da pagina inicial
    private JButton startButton; // botao para iniciar a compra de bilhetes
    private JTextArea skyBoundTextArea; // area de texto (nao utilizada no codigo fornecido)
    private JPanel panel2; // painel secundario (nao utilizado no codigo fornecido)

    // construtor da classe skyboundhomepage
    public SkyBoundHomePage() {
        panel1 = new JPanel(); // inicializa o painel principal
        panel1.setLayout(new BorderLayout()); // define o layout do painel como borderlayout
        panel1.setBackground(new Color(19, 47, 71)); // define a cor de fundo do painel
        panel1.setPreferredSize(new Dimension(800, 600)); // define o tamanho preferido do painel

        JPanel centerPanel = new JPanel(); // inicializa um painel central
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS)); // define o layout como boxlayout vertical
        centerPanel.setBackground(new Color(19, 47, 71)); // define a cor de fundo do painel central

        // tenta carregar e exibir o logo da skybound
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/skybound_logo.png")); // carrega a imagem do logo
            Image scaledImage = logoIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH); // redimensiona a imagem
            JLabel logoLabel = new JLabel(new ImageIcon(scaledImage)); // cria um label com a imagem redimensionada
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // centraliza o label horizontalmente
            centerPanel.add(Box.createVerticalGlue()); // adiciona espaco flexivel acima do logo
            centerPanel.add(logoLabel); // adiciona o logo ao painel central
            centerPanel.add(Box.createRigidArea(new Dimension(0, 20))); // adiciona um espaco rigido abaixo do logo
        } catch (Exception e) {
            System.out.println("Erro ao carregar o logo: " + e.getMessage()); // exibe mensagem de erro se o logo nao for carregado
        }

        // configura o botao de inicio
        startButton = new JButton("Compre seu bilhete ja"); // texto do botao
        startButton.setMaximumSize(new Dimension(200, 40)); // define o tamanho maximo do botao
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT); // centraliza o botao horizontalmente
        startButton.setBackground(new Color(0, 153, 255)); // define a cor de fundo do botao
        startButton.setForeground(Color.WHITE); // define a cor do texto do botao
        startButton.setFocusPainted(false); // remove o contorno de foco do botao

        // adiciona o botao ao painel central e espaco flexivel abaixo
        centerPanel.add(startButton);
        centerPanel.add(Box.createVerticalGlue());

        // adiciona o painel central ao painel principal
        panel1.add(centerPanel, BorderLayout.CENTER);

        // adiciona um listener ao botao para abrir a tela de compra de bilhetes
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CompraBilhete(); // abre a tela de compra de bilhetes
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(panel1); // obtem o frame principal
                if (topFrame != null) {
                    topFrame.dispose(); // fecha a janela atual
                }
            }
        });
    }

    // retorna o painel principal
    public JPanel getPanel1() {
        return panel1;
    }

    // metodo principal para iniciar a aplicacao
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoadingScreen loadingScreen = new LoadingScreen(); // cria uma tela de carregamento
            loadingScreen.startLoading(); // inicia a tela de carregamento
        });
    }
}