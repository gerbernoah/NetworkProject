package client.gui;

import client.Client;
import client.gui.components.GameLabel;
import client.gui.components.HintTextField;
import client.gui.components.ShipPlaceLabel;
import server.field.Ship;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.registry.Registry;
import java.util.Arrays;

public class PlaceField {

    private JFrame jFrame;
    private Container contentPane;

    private JPanel gamePanel = new JPanel(new GridLayout(10,10));
    private GameLabel[] gameComponents = new GameLabel[100];

    private JPanel shipPanel = new JPanel(new GridLayout(5,1));
    private ShipPlaceLabel[] shipPlaceLabels = new ShipPlaceLabel[5];

    private JLabel readyButton = new JLabel("Ready", SwingConstants.CENTER);

    private Ship[] ships = new Ship[5];
    private Ship[] tempShips = new Ship[5];
    private int multiplier = 1;

    private JTextField enterIpField = new HintTextField("Verbindungsadresse eingeben");

    private int selectedShip = 11;

    public PlaceField(JFrame jFrame) {
        this.jFrame = jFrame;
        this.contentPane = jFrame.getContentPane();
        setupPlaceField();
        setupShipField();
        setupButtons();

        for (GameLabel gameComponent : gameComponents)
        {
            gamePanel.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseClicked(MouseEvent e)
                {
                    if (e.getButton() == MouseEvent.BUTTON3)
                    {
                        if (multiplier == 1)
                        {
                            multiplier = 10;
                        } else if (multiplier == 10)
                        {
                            multiplier = 1;
                        }
                        contentPane.revalidate();
                    }
                }
            });
        }
        contentPane.requestFocus();
        for(int i = 0; i < 5; i++) {
            tempShips[i] = new Ship();
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
            gameComponents[i] = new GameLabel();
            int finalI = i;
            gameComponents[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(selectedShip != 11 && e.getButton() == MouseEvent.BUTTON1) {
                        Ship ship = tempShips[selectedShip];

                        boolean possible = true;
                        for(int i : ship.getPositions()) {
                            if (gameComponents[i].isShip() || !gameComponents[i].isPlaceable())
                            {
                                possible = false;
                                break;
                            }
                        }
                        if(possible) {
                            for (int i : ship.getPositions())
                                gameComponents[i].setShip(true);

                            int xStart = Math.max(0, ship.getxStartPos() - 1);
                            int yStart = Math.max(0, ship.getyStartPos() - 1);
                            int xEnd = Math.min(9, ship.getxEndPos() + 1);
                            int yEnd = Math.min(9, ship.getyEndPos() + 1);

                            for (int x = xStart; x <= xEnd; x++)
                                for (int y = yStart; y <= yEnd; y++)
                                    gameComponents[y * 10 + x].setPlaceable(false);

                            ships[selectedShip] = tempShips[selectedShip];

                            for (int z = 0; z < shipPlaceLabels.length; z++)
                                if (ships[z].placed())
                                    shipPlaceLabels[z].setBackground(new Color(0, 255, 0));

                            selectedShip = 11;
                        }
                    }
                    if(e.getButton() == MouseEvent.BUTTON3) {
                        if(multiplier == 1) {
                            multiplier = 10;
                        } else if(multiplier == 10) {
                            multiplier = 1;
                        }
                        contentPane.revalidate();
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    if(selectedShip != 11) {
                        tempShips[selectedShip].setPos(finalI, finalI + (shipPlaceLabels[selectedShip].getLength()*multiplier));
                        for (int i : tempShips[selectedShip].getPositions()) {
                            if(gameComponents[i].isShip() || !gameComponents[i].isPlaceable())
                                gameComponents[i].setBackground(Color.RED);
                            else
                                gameComponents[i].setBackground(Color.GREEN);
                        }
                        for (int z = 0; z < shipPlaceLabels.length; z++)
                            if (ships[z].placed())
                                shipPlaceLabels[z].setBackground(new Color(0, 255, 0));
                    }
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    for(GameLabel lbl : gameComponents) {
                        lbl.reloadColor(false);
                    }
                    for (int z = 0; z < shipPlaceLabels.length; z++)
                        if (ships[z].placed())
                            shipPlaceLabels[z].setBackground(new Color(0, 255, 0));
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
                    selectedShip = finalI;
                    Ship ship = ships[selectedShip];
                    int xStart = Math.max(0, ship.getxStartPos() - 1);
                    int yStart = Math.max(0, ship.getyStartPos() - 1);
                    int xEnd = Math.min(9, ship.getxEndPos() + 1);
                    int yEnd = Math.min(9, ship.getyEndPos() + 1);

                    for (int x = xStart; x <= xEnd; x++)
                        for (int y = yStart; y <= yEnd; y++)
                            gameComponents[y * 10 + x].setPlaceable(true);
                    for (int i : ship.getPositions())
                        gameComponents[i].setShip(false);

                    for (int z = 0; z < shipPlaceLabels.length; z++) {
                        if(ships[z].placed()) {
                            shipPlaceLabels[z].setBackground(new Color(0,255,0));
                        } else {
                            shipPlaceLabels[z].setBackground(Color.lightGray);
                        }
                    }
                    ships[selectedShip] = new Ship();
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    shipPlaceLabels[finalI].setBackground(Color.GRAY);
                    if (ships[finalI].placed())
                        shipPlaceLabels[finalI].setBackground(new Color(30, 150, 30));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    shipPlaceLabels[finalI].setBackground(Color.lightGray);
                    for (int z = 0; z < shipPlaceLabels.length; z++)
                        if (ships[z].placed())
                            shipPlaceLabels[z].setBackground(new Color(0, 255, 0));
                }
            });
        }
    }

    private void setupButtons() {
        enterIpField.setBounds(740, 500, 200, 50);
        contentPane.add(enterIpField);
        readyButton.setBounds(740, 550, 200, 50);
        readyButton.setBorder(new LineBorder(Color.BLACK));
        readyButton.setBackground(Color.lightGray);
        readyButton.setOpaque(true);
        contentPane.add(readyButton);
        readyButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (Ship ship : ships)
                {
                    if (!ship.placed())
                        return;
                }

                String[] input = enterIpField.getText().split(":");
                int port = Registry.REGISTRY_PORT;
                String host = "";
                if (input.length > 0)
                    host = input[0];
                if (input.length > 1)
                    port = Integer.parseInt(input[1]);
                Client client = new Client(jFrame, host, port);

                client.setShips(ships);
                Point[] points = new Point[ships.length];
                for (int i = 0; i < ships.length; i++)
                {
                    points[i] = new Point(ships[i].getStartPos(), ships[i].getEndPos());
                }
                client.placeShips(points);
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
        jf.setResizable(false);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        PlaceField placefield = new PlaceField(jf);
        jf.setContentPane(placefield.getContentPane());
    }
}
