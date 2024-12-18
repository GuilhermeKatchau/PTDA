package org.example;

import javax.swing.*;
import java.awt.*;

public class SkyBoundHomePageGestor {
    private JPanel panel1;
    private JButton ADICIONARVOOSButton;
    private JButton CONSULTARVOOSButton;

    public SkyBoundHomePageGestor() {
        JFrame frame = new JFrame("SkyBound Home");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setVisible(true);

        ADICIONARVOOSButton.addActionListener(e -> new SkyBoundGestaoVoos());
        CONSULTARVOOSButton.addActionListener(e -> mostrarVoos());
    }

    private void mostrarVoos() {
        JFrame frame = new JFrame("Voos Cadastrados");
        DefaultListModel<String> model = new DefaultListModel<>();
        for (Flight flight : FlightManager.getInstance().getFlights()) {
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
