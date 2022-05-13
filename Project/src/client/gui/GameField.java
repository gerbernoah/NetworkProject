package client.gui;

import client.Client;
import client.gui.components.GameLabel;
import client.gui.components.HintTextField;
import server.field.Ship;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GameField {

    private Container contentPane;
    private Client client;

    private JPanel[] gamePanel = new JPanel[2];
    private GameLabel[][] gameComponents = new GameLabel[2][100];

    private JTextArea chatArea = new JTextArea();
    private JTextField chatField = new HintTextField("Nachricht eingeben..");
    private JButton sendMessage = new JButton("Senden");

    private JLabel headerYou = new JLabel("Eigene Schiffe: ");
    private JLabel headerEnemy = new JLabel("Gegnerische Schiffe: ");

    private JPanel statsYou = new JPanel(new GridLayout(3, 2));
    private JPanel statsEnemy = new JPanel(new GridLayout(3, 2));

    private JLabel[] headerStats = new JLabel[2];

    private JLabel[] shotsYou = new JLabel[2];
    private JLabel[] hitsYou = new JLabel[2];
    private JLabel[] hitRateYou = new JLabel[2];

    private JLabel[] shotsEnemy = new JLabel[2];
    private JLabel[] hitsEnemy = new JLabel[2];
    private JLabel[] hitRateEnemy = new JLabel[2];

    public GameField(Container contentPane, Client client) {
        this.client = client;
        this.contentPane = contentPane;
        contentPane.setLayout(null);
        contentPane.removeAll();
        setup();

        /*
        Point[] shipPositions = new Point[]{
                new Point(3,4), new Point(94, 95),
                new Point(43, 46), new Point(64, 67), new Point(40, 80)
        };
        boolean x = client.placeShips(shipPositions);
        System.out.println(x);
        Ship[] ships = new Ship[]{
                new Ship(3,4), new Ship(94, 95),
                new Ship(43, 46), new Ship(64, 67), new Ship(40, 80)
        };
        setupShips(ships);
        */
    }

    private void setup() {
        contentPane.add(headerYou);
        contentPane.add(headerEnemy);
        headerYou.setBounds(50, 280, 300, 40);
        headerYou.setFont(new Font(Font.DIALOG, Font.BOLD, 25));
        headerEnemy.setBounds(654, 280, 300, 40);
        headerEnemy.setFont(new Font(Font.DIALOG, Font.BOLD, 25));
        setupGameFieldOnPanel(0);
        setupGameFieldOnPanel(1);
        setupChat();
        setupStats();
    }

    private void setupGameFieldOnPanel(int player) {
        gamePanel[player] = new JPanel(new GridLayout(10,10));
        gamePanel[player].setBorder(new LineBorder(Color.BLACK));
        gamePanel[player].setOpaque(true);
        gamePanel[player].setBackground(Color.BLACK);
        for(int i = 0; i < 100; i++) {
            gameComponents[player][i] = new GameLabel(i);
            gamePanel[player].add(gameComponents[player][i]);
            if(player == 1) {
                int finalI = i;
                gameComponents[player][i].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if(client.isPlayerOnTurn()) {
                            int shoot = client.shoot(finalI);
                            boolean hit = shoot != 0;
                            setGameComponentAsShot(player, finalI, hit);
                        }
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        gameComponents[player][finalI].setBackground(Color.decode("#5db5fc"));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        gameComponents[player][finalI].reloadColor(false);
                    }
                });
            }
        }
        contentPane.add(gamePanel[player]);

        switch (player) {
            case 0 : gamePanel[0].setBounds(50,320, 320, 320); break;
            case 1 : gamePanel[1].setBounds(654,320, 320, 320); break;
        }
    }

    private void setupChat() {
        contentPane.add(chatArea);
        contentPane.add(chatField);
        contentPane.add(sendMessage);
        chatArea.setBounds(50,50, 924, 150);
        chatArea.setEditable(false);
        chatField.setBounds(50, 210, 774, 40);
        sendMessage.setBounds(834, 210, 140, 40);
        chatArea.setBorder(new LineBorder(Color.black));
    }

    private void setupStats() {
        contentPane.add(statsYou);
        contentPane.add(statsEnemy);

        statsYou.setBounds(400, 360, 200, 110);
        statsEnemy.setBounds(400, 510, 200, 110);

        headerStats[0] = new JLabel("Deine Stats: ", SwingConstants.LEFT);
        headerStats[1] = new JLabel("Gegner Stats: ", SwingConstants.LEFT);
        contentPane.add(headerStats[0]);
        contentPane.add(headerStats[1]);
        headerStats[0].setBounds(400,320,160,40);
        headerStats[1].setBounds(400,470, 160,40);
        headerStats[0].setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        headerStats[1].setFont(new Font(Font.DIALOG, Font.BOLD, 20));

        shotsYou[0] = new JLabel("Geschossen: ");
        shotsYou[1] = new JLabel("0", SwingConstants.CENTER);
        hitsYou[0] = new JLabel("Getroffen: ");
        hitsYou[1] = new JLabel("0", SwingConstants.CENTER);
        hitRateYou[0] = new JLabel("Trefferquote: ");
        hitRateYou[1] = new JLabel("100%", SwingConstants.CENTER);

        shotsEnemy[0] = new JLabel("Geschossen: ");
        shotsEnemy[1] = new JLabel("0", SwingConstants.CENTER);
        hitsEnemy[0] = new JLabel("Getroffen: ");
        hitsEnemy[1] = new JLabel("0", SwingConstants.CENTER);
        hitRateEnemy[0] = new JLabel("Trefferquote: ");
        hitRateEnemy[1] = new JLabel("100%", SwingConstants.CENTER);

        statsYou.add(shotsYou[0]);
        statsYou.add(shotsYou[1]);
        statsYou.add(hitsYou[0]);
        statsYou.add(hitsYou[1]);
        statsYou.add(hitRateYou[0]);
        statsYou.add(hitRateYou[1]);

        statsEnemy.add(shotsEnemy[0]);
        statsEnemy.add(shotsEnemy[1]);
        statsEnemy.add(hitsEnemy[0]);
        statsEnemy.add(hitsEnemy[1]);
        statsEnemy.add(hitRateEnemy[0]);
        statsEnemy.add(hitRateEnemy[1]);
    }

    public Container getContentPane() {
        return contentPane;
    }

    public void setupShips(Ship[] ships) {
        for(Ship ship : ships) {
            for(int pos : ship.getPositions()) {
                gameComponents[0][pos].setShip(true);
            }
        }
        contentPane.repaint();
    }

    public void setGameComponentAsShot(int player, int id, boolean hit) {
        gameComponents[player][id].setHit(true);
        gameComponents[player][id].reloadColor(hit);
        for (MouseListener act : gameComponents[player][id].getMouseListeners()) {
            gameComponents[player][id].removeMouseListener(act);
        }
    }

    public void colorBorder(boolean ownBorder)
    {
        if (ownBorder)
        {
            gamePanel[0].setBackground(Color.BLACK);
            gamePanel[1].setBackground(Color.RED);
        }
        else
        {
            gamePanel[0].setBackground(Color.RED);
            gamePanel[1].setBackground(Color.BLACK);
        }
    }
}

class MainGameField {
    public static void main(String[] args) {
        JFrame jf = new JFrame();
        jf.setVisible(true);
        jf.setSize(1024,720);
        //GameField gamefield = new GameField(jf.getContentPane(), new Client());
        //jf.setContentPane(gamefield.getContentPane());
    }
}
