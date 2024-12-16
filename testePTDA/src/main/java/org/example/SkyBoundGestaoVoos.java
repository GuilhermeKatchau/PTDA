package org.example;

import javax.swing.*;

public class SkyBoundGestaoVoos extends JFrame {
    public SkyBoundGestaoVoos() {
        setTitle("Gestão de Voos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new SkyBoundGestaoVoos();
    }
}
