package client.gui;

import client.Client;
import client.gui.components.GameLabel;
import client.gui.components.ShipPlaceLabel;
import server.field.Ship;

import javax.sound.sampled.Line;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PlaceField {

    private Container contentPane;

    private JPanel gamePanel = new JPanel(new GridLayout(10,10));
    private GameLabel[] gameComponents = new GameLabel[100];

    private JPanel shipPanel = new JPanel(new GridLayout(5,1));
    private ShipPlaceLabel[] shipPlaceLabels = new ShipPlaceLabel[5];

    private JLabel readyButton = new JLabel("Ready", SwingConstants.CENTER);
    private JLabel reset = new JLabel("Reset", SwingConstants.CENTER);
    private JLabel joinCode = new JLabel("", SwingConstants.CENTER);

    private Ship[] ships = new Ship[5];

    public PlaceField(Container contentPane, Client client) {
        this.contentPane = contentPane;
        setupPlaceField();
        setupShipField();
        setupButtons();
    }

    private void setupPlaceField() {
        gamePanel.setBounds(50, 50, 550, 550);
        gamePanel.setBorder(new LineBorder(Color.BLACK));
        gamePanel.setBackground(Color.BLACK);
        gamePanel.setOpaque(true);
        contentPane.add(gamePanel);
        addGameComponentsToField();
    }

    private void addGameComponentsToField() {
        for(int i = 0; i < gameComponents.length; i++) {
            gameComponents[i] = new GameLabel(i);
            gameComponents[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
            gamePanel.add(gameComponents[i]);
        }
    }

    private void setupShipField() {
        shipPanel.setBounds(740, 50, 200, 250);
        shipPanel.setBorder(new LineBorder(Color.BLACK));
        shipPanel.setBackground(Color.GRAY);
        shipPanel.setOpaque(true);
        contentPane.add(shipPanel);
        addShipsToShipField();
    }

    private void addShipsToShipField() {
        shipPlaceLabels[0] = new ShipPlaceLabel("Schlachtschiff", 5);
        shipPlaceLabels[1] = new ShipPlaceLabel("Kreuzer", 4);
        shipPlaceLabels[2] = new ShipPlaceLabel("Fregatte", 3);
        shipPlaceLabels[3] = new ShipPlaceLabel("Fregatte", 3);
        shipPlaceLabels[4] = new ShipPlaceLabel("Minensucher", 2);
        for(ShipPlaceLabel lbl : shipPlaceLabels) {
            shipPanel.add(lbl);
        }
    }

    private void setupButtons() {
        readyButton.setBounds(740, 550, 200, 50);
        readyButton.setBorder(new LineBorder(Color.BLACK));
        reset.setBounds(740, 500, 200, 50);
        reset.setBorder(new LineBorder(Color.BLACK));
        joinCode.setBounds(740, 470, 200, 30);
        contentPane.add(readyButton);
        contentPane.add(reset);
        contentPane.add(joinCode);
    }

    public Container getContentPane() {
        return contentPane;
    }

}

class MainPlaceField {
    public static void main(String[] args) {
        JFrame jf = new JFrame();
        jf.setVisible(true);
        jf.setSize(1024,720);
        jf.setLayout(null);
        PlaceField placefield = new PlaceField(jf.getContentPane(), new Client());
        jf.setContentPane(placefield.getContentPane());
    }
}
