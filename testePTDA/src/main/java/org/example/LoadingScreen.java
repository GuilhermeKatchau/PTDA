package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoadingScreen extends JFrame {
    private JProgressBar progressBar;
    private Timer timer;
    private final int TIMER_DELAY = 30;
    private int progress = 0;

    public LoadingScreen() {
        setUndecorated(true);
        setSize(500, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(19, 47, 71));

        JPanel logoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon logoIcon = new ImageIcon(getClass().getResource("/skybound_logo.png"));
                Image logo = logoIcon.getImage();
                int x = (getWidth() - 400) / 2;
                int y = (getHeight() - 400) / 2;
                g.drawImage(logo, x, y, 400, 400, this);
            }
        };
        logoPanel.setBackground(new Color(19, 47, 71));

        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(0, 153, 255)); // Azul do logo
        progressBar.setBackground(Color.WHITE);
        progressBar.setPreferredSize(new Dimension(400, 20));

        JPanel progressPanel = new JPanel();
        progressPanel.setBackground(new Color(19, 47, 71));
        progressPanel.add(progressBar);

        mainPanel.add(logoPanel, BorderLayout.CENTER);
        mainPanel.add(progressPanel, BorderLayout.SOUTH);

        add(mainPanel);

        timer = new Timer(TIMER_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                progress += 1;
                progressBar.setValue(progress);

                if (progress >= 100) {
                    timer.stop();
                    dispose();
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

    public void startLoading() {
        setVisible(true);
        timer.start();
    }

}