package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SkyBoundHomePage {

    private JPanel panel1;
    private JButton startButton;
    private JTextArea skyBoundTextArea;
    private JPanel panel2;

    public SkyBoundHomePage() {
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Quando o botão for clicado, cria uma nova instância de CompraBilhete
                new CompraBilhete();
                // Fecha a janela atual
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(panel1);
                if (topFrame != null) {
                    topFrame.dispose();
                }
            }
        });
    }
public static void main(String[] args) {
    // Criar a janela e mostrá-la
    JFrame frame = new JFrame("SkyBound HomePage");
    frame.setContentPane(new SkyBoundHomePage().panel1);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
}
}