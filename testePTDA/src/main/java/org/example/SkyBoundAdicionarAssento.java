package org.example;

import javax.swing.*;
import java.awt.*;

public class SkyBoundAdicionarAssento extends JFrame {
    private JPanel panel1;
    private JButton a21Button;
    private JButton a17Button;
    private JButton a13Button;
    private JButton a9Button;
    private JButton a5Button;
    private JButton a1Button;
    private JButton a10Button;
    private JButton a6Button;
    private JButton a2Button;
    private JButton a14Button;
    private JButton a18Button;
    private JButton a22Button;
    private JButton a16Button;
    private JButton a12Button;
    private JButton a8Button;
    private JButton a4Button;
    private JButton a20Button;
    private JButton a24Button;
    private JButton a11Button;
    private JButton a7Button;
    private JButton a3Button;
    private JButton a15Button;
    private JButton a19Button;
    private JButton a23Button;

    public SkyBoundAdicionarAssento() {
        setTitle("Escolhe Assento");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        adicionarEventosBotoes();

        setVisible(true);
    }

    private void adicionarEventosBotoes() {
        JButton[] botoesAssentos = new JButton[]{
                a1Button, a2Button, a3Button, a4Button, a5Button, a6Button,
                a7Button, a8Button, a9Button, a10Button, a11Button, a12Button,
                a13Button, a14Button, a15Button, a16Button, a17Button, a18Button,
                a19Button, a20Button, a21Button, a22Button, a23Button, a24Button
        };

        for (int i = 0; i < botoesAssentos.length; i++) {
            int numeroAssento = i + 1; // Números de 1 a 24
            botoesAssentos[i].addActionListener(e -> processarEscolhaAssento(numeroAssento));
        }
    }

    private void processarEscolhaAssento(int numeroAssento) {
        JOptionPane.showMessageDialog(this, "Você escolheu o assento: " + numeroAssento);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SkyBoundAdicionarAssento());
    }

}
