package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SkyBoundHomePage {
    public JPanel panel1;
    private JButton startButton;
    private JTextArea skyBoundTextArea;
    private JPanel panel2;

    public SkyBoundHomePage() {
        panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());
        panel1.setBackground(new Color(19, 47, 71));
        panel1.setPreferredSize(new Dimension(800, 600));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(19, 47, 71));

        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/skybound_logo.png"));
            Image scaledImage = logoIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            centerPanel.add(Box.createVerticalGlue());
            centerPanel.add(logoLabel);
            centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        } catch (Exception e) {
            System.out.println("Erro ao carregar o logo: " + e.getMessage());
        }

        startButton = new JButton("Compre seu bilhete já");
        startButton.setMaximumSize(new Dimension(200, 40));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setBackground(new Color(0, 153, 255));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);

        centerPanel.add(startButton);
        centerPanel.add(Box.createVerticalGlue());

        panel1.add(centerPanel, BorderLayout.CENTER);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CompraBilhete();
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(panel1);
                if (topFrame != null) {
                    topFrame.dispose();
                }
            }
        });
    }

    public JPanel getPanel1() {
        return panel1;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoadingScreen loadingScreen = new LoadingScreen();
            loadingScreen.startLoading();
        });
    }
}