package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoadingScreen extends JFrame {
    private JProgressBar progressBar; // barra de progresso
    private Timer timer; // temporizador para controlar o progresso
    private final int TIMER_DELAY = 30; // intervalo de tempo para atualizar a barra de progresso
    private int progress = 0; // valor atual do progresso

    // construtor da classe
    public LoadingScreen() {
        setUndecorated(true); // remove a borda da janela
        setSize(500, 600); // define o tamanho da janela
        setLocationRelativeTo(null); // centraliza a janela na tela

        // painel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(19, 47, 71)); // cor de fundo

        // painel do logo
        JPanel logoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // carrega a imagem do logo
                ImageIcon logoIcon = new ImageIcon(getClass().getResource("/skybound_logo.png"));
                Image logo = logoIcon.getImage();
                // centraliza o logo no painel
                int x = (getWidth() - 400) / 2;
                int y = (getHeight() - 400) / 2;
                g.drawImage(logo, x, y, 400, 400, this);
            }
        };
        logoPanel.setBackground(new Color(19, 47, 71)); // cor de fundo do painel do logo

        // barra de progresso
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true); // exibe o valor do progresso
        progressBar.setForeground(new Color(0, 153, 255)); // cor da barra de progresso (azul do logo)
        progressBar.setBackground(Color.WHITE); // cor de fundo da barra de progresso
        progressBar.setPreferredSize(new Dimension(400, 20)); // tamanho da barra de progresso

        // painel da barra de progresso
        JPanel progressPanel = new JPanel();
        progressPanel.setBackground(new Color(19, 47, 71)); // cor de fundo
        progressPanel.add(progressBar);

        // adiciona os paineis ao painel principal
        mainPanel.add(logoPanel, BorderLayout.CENTER);
        mainPanel.add(progressPanel, BorderLayout.SOUTH);

        // adiciona o painel principal a janela
        add(mainPanel);

        // inicializa o temporizador
        timer = new Timer(TIMER_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                progress += 1; // incrementa o progresso
                progressBar.setValue(progress); // atualiza a barra de progresso

                // quando o progresso chegar a 100, fecha a tela de carregamento e abre a home page
                if (progress >= 100) {
                    timer.stop(); // para o temporizador
                    dispose(); // fecha a tela de carregamento
                    SwingUtilities.invokeLater(() -> {
                        JFrame frame = new JFrame("SkyBound HomePage");
                        frame.setContentPane(new SkyBoundHomePage().panel1);
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        frame.pack();
                        frame.setVisible(true);
                    });
                }
            }
        });
    }

    // inicia a tela de carregamento
    public void startLoading() {
        setVisible(true); // torna a janela visivel
        timer.start(); // inicia o temporizador
    }
}