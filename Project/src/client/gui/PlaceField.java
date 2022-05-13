package client.gui;

import client.Client;
import client.gui.components.GameLabel;
import client.gui.components.ShipPlaceLabel;
import org.w3c.dom.ls.LSOutput;
import server.field.Ship;

import javax.sound.sampled.Line;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PlaceField {

    private Container contentPane;

    private JPanel gamePanel = new JPanel(new GridLayout(10,10));
    private GameLabel[] gameComponents = new GameLabel[100];

    private JPanel shipPanel = new JPanel(new GridLayout(5,1));
    private ShipPlaceLabel[] shipPlaceLabels = new ShipPlaceLabel[5];

    private JLabel readyButton = new JLabel("Ready", SwingConstants.CENTER);

    private Ship[] ships = new Ship[5];
    private int multiplier = 1;

    private int selectedShip = 11;

    public PlaceField(Container contentPane) {
        this.contentPane = contentPane;
        setupPlaceField();
        setupShipField();
        setupButtons();
        contentPane.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyChar() == 'r') {
                    if(multiplier == 1) {
                        multiplier = 10;
                    } else if(multiplier == 10) {
                        multiplier = 1;
                    }
                    contentPane.revalidate();
                    System.out.println(multiplier);
                }
            }
        });
        contentPane.requestFocus();
        for(int i = 0; i < 5; i++) {
            ships[i] = new Ship();
        }
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
            int finalI = i;
            gameComponents[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(selectedShip != 11) {
                        boolean possible = true;
                        for(int i : ships[selectedShip].getPositions()) {
                            if(gameComponents[i].isShip()) possible = false;
                        }
                        if(possible) {
                            for (int i : ships[selectedShip].getPositions()) {
                                gameComponents[i].setShip(true);
                            }
                            selectedShip = 11;
                        }

                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    if(selectedShip != 11) {
                        ships[selectedShip].setPos(finalI, finalI + (shipPlaceLabels[selectedShip].getLength()*multiplier));
                        for (int i : ships[selectedShip].getPositions()) {
                            if(gameComponents[i].isShip()) gameComponents[i].setBackground(Color.RED);
                            if(!gameComponents[i].isShip()) gameComponents[i].setBackground(Color.GREEN);
                            System.out.println(i);
                        }
                    }
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    for(GameLabel lbl : gameComponents) {
                        lbl.reloadColor(false);
                    }
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
        shipPlaceLabels[3] = new ShipPlaceLabel("Zerstörer", 3);
        shipPlaceLabels[4] = new ShipPlaceLabel("Minensucher", 2);
        for(ShipPlaceLabel lbl : shipPlaceLabels) {
            shipPanel.add(lbl);
        }
        for(int i = 0; i < 5; i++) {
            int finalI = i;
            shipPlaceLabels[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(selectedShip != 11) {
                        selectedShip = finalI;
                        for(int i : ships[selectedShip].getPositions()) {
                            gameComponents[i].setShip(false);
                        }
                        System.out.println(selectedShip);
                    } else if (selectedShip == 11) {
                        selectedShip = finalI;
                        for(int i : ships[selectedShip].getPositions()) {
                            gameComponents[i].setShip(false);
                        }
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    shipPlaceLabels[finalI].setBackground(Color.GRAY);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    shipPlaceLabels[finalI].setBackground(Color.lightGray);
                }
            });
        }
    }

    private void setupButtons() {
        readyButton.setBounds(740, 550, 200, 50);
        readyButton.setBorder(new LineBorder(Color.BLACK));
        readyButton.setBackground(Color.lightGray);
        readyButton.setOpaque(true);
        contentPane.add(readyButton);
        readyButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //TODO: READY VALIDATE
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                readyButton.setBackground(Color.GRAY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                readyButton.setBackground(Color.LIGHT_GRAY);
            }
        });
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
        PlaceField placefield = new PlaceField(jf.getContentPane());
        jf.setContentPane(placefield.getContentPane());
    }
}
