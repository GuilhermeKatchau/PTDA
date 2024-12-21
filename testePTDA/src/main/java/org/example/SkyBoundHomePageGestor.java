package org.example;

import javax.swing.*;
import java.awt.*;

public class SkyBoundHomePageGestor {
    private JPanel panel1;
    private JButton addFlightsButton;
    private JButton viewFlightsButton;

    public SkyBoundHomePageGestor() {
        JFrame frame = new JFrame("SkyBound Home");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setVisible(true);

        addFlightsButton.addActionListener(e -> new SkyBoundGestaoVoos());
        viewFlightsButton.addActionListener(e -> mostrarVoos());
    }

    private void mostrarVoos() {
        JFrame frame = new JFrame("Voos Cadastrados");
        DefaultListModel<String> model = new DefaultListModel<>();
        for (Flight flight : Flight.getFlights()) {
            model.addElement(flight.toString());
        }

        JList<String> list = new JList<>(model);
        frame.add(new JScrollPane(list), BorderLayout.CENTER);
        frame.setSize(400, 300);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new SkyBoundHomePageGestor();
    }
}
